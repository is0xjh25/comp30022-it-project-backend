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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService{

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User verify(LoginRequest loginRequest) throws BadCredentialsException{
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email",loginRequest.getEmail());
        User user = getOne(userQueryWrapper);
        if (!check(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("The user name or password is not correct.");
        }
        return user;
    }


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

    public User fromUserDTO(UserDTO userDTO){
        User user = new User();
        NullAwareBeanUtilsBean.copyProperties(userDTO, user);
        return user;
    }

    public boolean check(String currentPassword, String password) {
        return this.passwordEncoder.matches(currentPassword, password);
    }

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

    public Integer getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (Integer)authentication.getPrincipal();
        }
        return null;
    }
}
