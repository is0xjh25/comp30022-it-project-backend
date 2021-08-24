package tech.crm.crmserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
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
                .deleteCookies("JSESSIONID");

        //login
        http.formLogin()
                .loginProcessingUrl("/user/login")
                //login with email instead of username
                .usernameParameter("email")
                .and().authorizeRequests()
                //permit without login
                .antMatchers("/user/login").permitAll()
                //permit all after login
                .anyRequest().authenticated()
                //token
                .and().rememberMe()
                .alwaysRemember(true)
                .tokenRepository(persistentTokenRepository())
                //valid for one month
                .tokenValiditySeconds(60*60*24*30)
                .userDetailsService(userDetailsService);

        http.csrf().disable();
    }

    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

}
