package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.constants.EmailConstants;
import tech.crm.crmserver.common.constants.TimeZoneConstants;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.Status;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.dto.UserRegisterDTO;
import tech.crm.crmserver.dto.UserUpdateDTO;
import tech.crm.crmserver.exception.LoginBadCredentialsException;
import tech.crm.crmserver.exception.UserAlreadyExistException;
import tech.crm.crmserver.exception.UserNotActiveException;
import tech.crm.crmserver.exception.UserNotExistException;
import tech.crm.crmserver.mapper.UserMapper;
import tech.crm.crmserver.service.MailService;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 *  serviceImpl for User service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenKeyService tokenKeyService;

    /**
     * the verify url prefix defined in property
     */
    @Value("${spring.mail.active}")
    public String verifyUrl;


    /**
     * Verify whether the email and password is correct
     *
     * @param loginRequest login form for login request
     * @return login user
     */
    public User verify(LoginRequest loginRequest){
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email",loginRequest.getEmail());
        User user = getOne(userQueryWrapper);
        if (user == null || !check(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginBadCredentialsException();
        }
        return user;
    }

    /**
     * Login and return the token
     *
     * @param loginRequest login form for login request
     * @return Token
     */
    @Override
    public String login(LoginRequest loginRequest) {
        //verify the user
        User user = verify(loginRequest);
        dealPendingUser(user);
        //return the token
        return tokenKeyService.createToken(user);
    }

    /**
     * Register the user
     *
     * @param user user need for register
     * @return null if fail, return user when successfully register
     */
    public User register(User user){
        //encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(Status.PENDING);
        try {
            save(user);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("email",user.getEmail());
            user = getOne(userQueryWrapper);
            sendActivationEmail(user);
        }
        catch (Exception e){
            throw new UserAlreadyExistException();
        }

        return user;
    }

    /**
     * Transfer a userRegisterDTO to user class
     *
     * @param userRegisterDTO userDTO class
     * @return user class
     */
    public User fromUserRegisterDTO(UserRegisterDTO userRegisterDTO){
        User user = new User();
        NullAwareBeanUtilsBean.copyProperties(userRegisterDTO, user);
        return user;
    }

    /**
     * Check whether the encoded of current Password matches the password
     *
     * @param currentPassword raw Password
     * @param password encodedPassword
     * @return whether the password matches
     */
    public boolean check(String currentPassword, String password) {
        return this.passwordEncoder.matches(currentPassword, password);
    }

    /**
     * Override the function in UserDetailsService
     *
     * @param email email of the user
     * @return UserDetails
     * @throws UsernameNotFoundException means user not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //find User in database by email
        QueryWrapper<User> wrapper = new QueryWrapper<User>();
        wrapper.eq("email",email);
        User user = getBaseMapper().selectOne(wrapper);

        //user not exist
        if(user == null){
            throw new UsernameNotFoundException("User not exist!");
        }

        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),auths);
    }

    /**
     * Get user Id from SecurityContextHolder
     * the Id was stored in the SecurityContextHolder at tech/crm/crmserver/security/JwtAuthorizationFilter.java
     *
     * @return user id
     */
    public Integer getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (Integer)authentication.getPrincipal();
        }
        return null;
    }

    /**
     * generate a random password and store encrypted password into database <br/>
     * then send the password to the email<br/>
     * and delete all the token of this user in database
     *
     * @param email the email of user who needs reset password
     */
    @Override
    public void resetPassword(String email) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("email",email);
        User user = this.getOne(wrapper);
        if(user == null){
            throw new UserNotExistException();
        }
        dealPendingUser(user);
        //delete token
        tokenKeyService.deleteTokenByUser(user.getId());
        //generate password
        String rawPassword = passwordEncoder.encode(UUID.randomUUID().toString());
        String encryptPassword = passwordEncoder.encode(rawPassword);

        //store password
        wrapper.set("password",encryptPassword);
        update(wrapper);
        //send email
        mailService.sendSimpleMail(email, EmailConstants.EMAIL_RESET_PASSWORD_TITLE, rawPassword);
    }

    /**
     * update the recent activity time of user
     *
     * @param userId user id
     */
    @Override
    public void updateRecentActivity(Integer userId) {
        //update recent activity
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id", userId)
                .set("recent_activity", LocalDateTime.now(TimeZoneConstants.ZONE));
        try {
            update(wrapper);
        }
        catch (Exception e){
            throw new UserNotExistException();
        }
    }

    /**
     * update the not null properties in userUpdateDTO
     *
     * @param userUpdateDTO user update information
     */
    @Override
    public void updateUser(UserUpdateDTO userUpdateDTO) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",getId());
        Map<String, String> map = new ObjectMapper().convertValue(userUpdateDTO, Map.class);
        //translate to underscore
        PropertyNamingStrategy.SnakeCaseStrategy snakeCaseStrategy = new PropertyNamingStrategy.SnakeCaseStrategy();
        for(Map.Entry<String,String> entry : map.entrySet()){
            if(entry.getValue() != null && !entry.getKey().equals("password")){
                updateWrapper.set(snakeCaseStrategy.translate(entry.getKey()),entry.getValue());
            }
        }
        //encode the password
        String rawPassword = userUpdateDTO.getPassword();
        if(rawPassword != null){
            String encodePassword = passwordEncoder.encode(rawPassword);
            updateWrapper.set("password",encodePassword);
            //clean token and notify the user
            tokenKeyService.deleteTokenByUser(getId());
            mailService.sendSimpleMail(getById(getId()).getEmail(), EmailConstants.SAFETY_NOTICE,
                    EmailConstants.SAFETY_NOTICE_CONTENT);
        }

        //update to database
        update(updateWrapper);
    }

    /**
     * check whether the user status is pending<br/>
     * if the status is pending, resend verify email and throw error
     *
     * @param user user whose status is pending
     */
    @Override
    public void dealPendingUser(User user) {
        //not pending
        if(!user.getStatus().equals(Status.PENDING)){
            return;
        }
        //resend verify email
        sendActivationEmail(user);
        throw new UserNotActiveException(HttpStatus.FORBIDDEN);
    }

    /**
     * send activation url to user's email
     * @param user user need to active
     */
    private void sendActivationEmail(User user){
        String url = this.verifyUrl + "?token=" + tokenKeyService.createToken(user).replace(SecurityConstants.TOKEN_PREFIX,"");
        mailService.sendSimpleMail(user.getEmail(), EmailConstants.ACTIVE_ACCOUNT_TITLE, url);
    }

    /**
     * active the user
     *
     * @param userId user id of user need to active
     */
    @Override
    public void activePendingUser(Integer userId) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",userId).set("status", Status.ACTIVE.getStatus());
        try {
            update(wrapper);
        }
        catch (Exception e){
            throw new UserNotExistException();
        }
    }

    /**
     * return page of UserPermissionDTO by wrapper from permission
     *
     * @param page         page config
     * @param queryWrapper wrapper
     * @return return page of UserPermissionDTO by wrapper
     */
    @Override
    public Page<UserPermissionDTO> getUserPermissionDTO(Page<?> page, Wrapper<User> queryWrapper) {
        return baseMapper.getUserPermissionDTO(page,queryWrapper);
    }

    /**
     * return page of UserPermissionDTO by wrapper from belong to
     *
     * @param page         page config
     * @param queryWrapper wrapper
     * @return return page of UserPermissionDTO by wrapper
     */
    @Override
    public Page<UserPermissionDTO> getUserPermissionDTOInOrganization(Page<?> page, Wrapper<User> queryWrapper) {
        return baseMapper.getUserPermissionDTOInOrganization(page,queryWrapper);
    }
}
