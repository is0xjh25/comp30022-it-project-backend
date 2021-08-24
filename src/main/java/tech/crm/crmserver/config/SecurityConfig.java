package tech.crm.crmserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import tech.crm.crmserver.handler.LoginAuthenticationFailureHandler;
import tech.crm.crmserver.handler.LoginAuthenticationSuccessHandler;
import tech.crm.crmserver.handler.MyLogoutSuccessHandler;
import tech.crm.crmserver.service.impl.TokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        TokenRepositoryImpl TokenRepositoryImpl = new TokenRepositoryImpl();
        TokenRepositoryImpl.setDataSource(dataSource);
        return TokenRepositoryImpl;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(password());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //logout
        http.logout()
                .logoutUrl("/user/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(new MyLogoutSuccessHandler());

        //login
        http.formLogin()
                .loginProcessingUrl("/user/login")
                .successHandler(new LoginAuthenticationSuccessHandler())
                .failureHandler(new LoginAuthenticationFailureHandler())
                //login with email instead of username
                .usernameParameter("email")
                .and().authorizeRequests()
                //permit without login
                .antMatchers("/user/login").permitAll()
                //need token to get permission
                //permit all after login
                .anyRequest().rememberMe()
                //token
                .and().rememberMe()
                .alwaysRemember(true)
                .tokenRepository(persistentTokenRepository())
                //valid for one month
                .tokenValiditySeconds(60*60*24*30)
                .userDetailsService(userDetailsService);

        //general
        http.csrf().disable()
                // Don't keep track of session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

}
