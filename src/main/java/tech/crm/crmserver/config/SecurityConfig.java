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
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import tech.crm.crmserver.handler.LoginAuthenticationFailureHandler;
import tech.crm.crmserver.handler.LoginAuthenticationSuccessHandler;
import tech.crm.crmserver.handler.MyLogoutSuccessHandler;
import tech.crm.crmserver.security.PersistentTokenBasedRememberMeServicesImpl;
import tech.crm.crmserver.security.TokenRepositoryImpl;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    /**
     *
     * @return
     */
    @Bean
    public PersistentTokenBasedRememberMeServicesImpl rememberMeServices() {
        PersistentTokenBasedRememberMeServicesImpl rememberMeServices =
                new PersistentTokenBasedRememberMeServicesImpl(userDetailsService,persistentTokenRepository());
        rememberMeServices.setParameter("this_rememberMeParameter");
        rememberMeServices.setCookieName("this_rememberMeParameter");
        //valid for a week
        rememberMeServices.setTokenValiditySeconds(60*60*24*7);
        //rememberMeServices.setUseSecureCookie(this.useSecureCookie);
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.afterPropertiesSet();
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new TokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        return jdbcTokenRepositoryImpl;
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
                .addLogoutHandler(rememberMeServices())
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
                .rememberMeServices(rememberMeServices());

        //general
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

}
