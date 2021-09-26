package tech.crm.crmserver.security;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import tech.crm.crmserver.common.response.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint
 * @author Lingxiao Li
 */
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * dont give Token or Token is invalid,
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
        Result<Object> fail = Result.fail("dont give Token or Token is invalid", HttpStatus.UNAUTHORIZED);
        response.getWriter().print(new Gson().toJson(fail));
    }
}
