package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.crm.crmserver.dao.User;
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
@Service("userDetailsService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, UserDetailsService {

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
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                new BCryptPasswordEncoder().encode(user.getPassword()),auths);
    }

}
