package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserRegisterDTO;
import tech.crm.crmserver.exception.LoginBadCredentialsException;
import tech.crm.crmserver.exception.UserAlreadyExistException;
import tech.crm.crmserver.exception.UserNotExistException;
import tech.crm.crmserver.mapper.UserMapper;
import tech.crm.crmserver.service.MailService;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
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

    private static String EMAIL_TITLE = "Your new Password for ConnecTi";

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
        try {
            save(user);
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("email",user.getEmail());
            user = getOne(userQueryWrapper);
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
        //delete token
        tokenKeyService.deleteTokenByUser(user.getId());
        //generate password
        String rawPassword = passwordEncoder.encode(UUID.randomUUID().toString());
        String encryptPassword = passwordEncoder.encode(rawPassword);

        //store password
        wrapper.set("password",encryptPassword);
        update(wrapper);
        //send email
        mailService.sendSimpleMail(email,EMAIL_TITLE,rawPassword);
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
                .set("recent_activity", LocalDateTime.now());
        try {
            update(wrapper);
        }
        catch (Exception e){
            throw new UserNotExistException();
        }
    }
}
