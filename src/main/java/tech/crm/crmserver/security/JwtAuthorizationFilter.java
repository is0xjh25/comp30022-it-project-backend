package tech.crm.crmserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Jwt filter for verify the Token when do auto login. modified from JwtAuthorizationFilter created by shuang.kou
 * @author Lingxiao Li
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {


    private final TokenKeyService tokenKeyService;

    private final UserService userService;

    /**
     * Constructor.
     */
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenKeyService tokenKeyService, UserService userService) {
        super(authenticationManager);
        this.tokenKeyService = tokenKeyService;
        this.userService = userService;
    }

    /**
     * verify the token and auto login. Give the user correspond permission
     * @param request the Http Servlet Request
     * @param response the Http Servlet Response
     * @param chain the filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String token = request.getHeader(SecurityConstants.TOKEN_HEADER);
        if (token == null || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
            return;
        }
        String tokenValue = token.replace(SecurityConstants.TOKEN_PREFIX, "");
        UsernamePasswordAuthenticationToken authentication = null;
        try {
            authentication = tokenKeyService.getAuthentication(tokenValue);
            userService.updateRecentActivity((Integer) authentication.getPrincipal());
        } catch (Exception e) {
            logger.error("Invalid jwt : " + e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }
}


