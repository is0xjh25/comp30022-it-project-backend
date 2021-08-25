package tech.crm.crmserver.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.*;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class PersistentTokenBasedRememberMeServicesImpl extends PersistentTokenBasedRememberMeServices {

    @Autowired
    public UserService userService;

    private PersistentTokenRepository tokenRepository = new InMemoryTokenRepositoryImpl();

    public PersistentTokenBasedRememberMeServicesImpl(String key, UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super(key, userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
    }

    public Integer getValidTime(){
        return this.getTokenValiditySeconds();
    }
    public User gotUserFromRequest(HttpServletRequest request){
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie == null) {
            return null;
        }
        this.logger.debug("Remember-me cookie detected ");
        if (rememberMeCookie.length() == 0) {
            this.logger.debug("Cookie was empty");
            return null;
        }
        try {
            String[] cookieTokens = decodeCookie(rememberMeCookie);
            this.logger.debug("Remember-me cookie accepted  in logout");
            if (cookieTokens.length != 2) {
                throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
                        + Arrays.asList(cookieTokens) + "'");
            }
            String presentedSeries = cookieTokens[0];
            TokenRepositoryImpl tokenRepository = (TokenRepositoryImpl)this.tokenRepository;
            String email = tokenRepository.getUsernameFromSeries(presentedSeries);
            QueryWrapper<User> wrapper = new QueryWrapper<User>();
            wrapper.eq("email",email);
            User user = userService.getBaseMapper().selectOne(wrapper);
            return user;
        }
        catch (CookieTheftException ex) {
            throw ex;
        }
        catch (UsernameNotFoundException ex) {
            this.logger.debug("Remember-me login was valid but corresponding user not found.", ex);
        }
        catch (InvalidCookieException ex) {
            this.logger.debug("Invalid remember-me cookie: " + ex.getMessage());
        }
        catch (AccountStatusException ex) {
            this.logger.debug("Invalid UserDetails: " + ex.getMessage());
        }
        catch (RememberMeAuthenticationException ex) {
            this.logger.debug(ex.getMessage());
        }
        return null;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        super.logout(request, response, authentication);
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie == null) {
            return;
        }
        this.logger.debug("Remember-me cookie detected in logout");
        if (rememberMeCookie.length() == 0) {
            this.logger.debug("Cookie was empty");
            cancelCookie(request, response);
            return;
        }
        try {
            String[] cookieTokens = decodeCookie(rememberMeCookie);
            this.logger.debug("Remember-me cookie accepted  in logout");
            if (cookieTokens.length != 2) {
                throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '"
                        + Arrays.asList(cookieTokens) + "'");
            }
            String presentedSeries = cookieTokens[0];
            this.tokenRepository.removeUserTokens(presentedSeries);
        }
        catch (CookieTheftException ex) {
                cancelCookie(request, response);
                throw ex;
        }
		catch (UsernameNotFoundException ex) {
                this.logger.debug("Remember-me login was valid but corresponding user not found.", ex);
        }
		catch (InvalidCookieException ex) {
                this.logger.debug("Invalid remember-me cookie: " + ex.getMessage());
        }
		catch (AccountStatusException ex) {
                this.logger.debug("Invalid UserDetails: " + ex.getMessage());
        }
		catch (RememberMeAuthenticationException ex) {
                this.logger.debug(ex.getMessage());
        }
    }
}
