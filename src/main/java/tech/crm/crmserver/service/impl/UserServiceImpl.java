package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserDTO;
import tech.crm.crmserver.mapper.UserMapper;
import tech.crm.crmserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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

    /**
     * verify whether the email and password is correct
     * @param loginRequest login form for login request
     * @return login user
     */
    public User verify(LoginRequest loginRequest) throws BadCredentialsException{
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email",loginRequest.getEmail());
        User user = getOne(userQueryWrapper);
        if (user == null || !check(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("The user name or password is not correct.");
        }
        return user;
    }


    /**
     * register the user
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
            return null;
        }
        return user;
    }

    /**
     * transfer a userDTO to user class
     * @param userDTO userDTO class
     * @return user class
     */
    public User fromUserDTO(UserDTO userDTO){
        User user = new User();
        NullAwareBeanUtilsBean.copyProperties(userDTO, user);
        return user;
    }

    /**
     * check whether the encoded of current Password matches the password
     * @param currentPassword raw Password
     * @param password encodedPassword
     * @return whether the password matches
     */
    public boolean check(String currentPassword, String password) {
        return this.passwordEncoder.matches(currentPassword, password);
    }

    /**
     * Override the function in UserDetailsService
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
            throw new UsernameNotFoundException("User not exist！");
        }

        List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("user");
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),auths);
    }

    /**
     * get user Id from SecurityContextHolder
     * the Id was stored in the SecurityContextHolder at tech/crm/crmserver/security/JwtAuthorizationFilter.java
     * @return user id
     */
    public Integer getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (Integer)authentication.getPrincipal();
        }
        return null;
    }
}
