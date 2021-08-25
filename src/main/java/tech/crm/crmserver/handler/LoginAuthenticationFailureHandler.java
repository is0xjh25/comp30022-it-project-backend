package tech.crm.crmserver.handler;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, AuthenticationException e) throws
            IOException, ServletException {
        //set status to 400(BAD_REQUEST)
        httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        //write json response
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("code", "400");
        map.put("status", "BAD_REQUEST");
        map.put("msg","Invalid username/password supplied!");
        map.put("data","");
        Gson gson = new Gson();
        httpServletResponse.getWriter().write(gson.toJson(map));
    }
}
