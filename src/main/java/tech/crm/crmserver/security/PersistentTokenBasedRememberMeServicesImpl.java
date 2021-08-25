package tech.crm.crmserver.security;

import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;

public class PersistentTokenBasedRememberMeServicesImpl extends PersistentTokenBasedRememberMeServices {

    private PersistentTokenRepository tokenRepository = new InMemoryTokenRepositoryImpl();

    private String key = "fewafasbfda";

    //private String key = UUID.randomUUID().toString();

    public PersistentTokenBasedRememberMeServicesImpl(UserDetailsService userDetailsService, PersistentTokenRepository tokenRepository) {
        super("fewafasbfda", userDetailsService, tokenRepository);
        this.tokenRepository = tokenRepository;
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
