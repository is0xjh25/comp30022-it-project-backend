package tech.crm.crmserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.crm.crmserver.security.JwtAccessDeniedHandler;
import tech.crm.crmserver.security.JwtAuthenticationEntryPoint;
import tech.crm.crmserver.security.JwtAuthorizationFilter;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;

/**
 * <p>
 * the Configuration for Spring Security
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public TokenKeyService tokenKeyService;

    @Autowired
    public UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        
        http.cors().and().authorizeRequests()
                //permit without login
                .antMatchers("/user/login").permitAll()
                .antMatchers(HttpMethod.POST,"/user","/user/resetPassword").permitAll()
                //need token to get permission
                //permit all after login
                .anyRequest().authenticated()
                .and()
                //JWT
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), tokenKeyService, userService))
                //exception handling
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());

        //general
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }


    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

}
