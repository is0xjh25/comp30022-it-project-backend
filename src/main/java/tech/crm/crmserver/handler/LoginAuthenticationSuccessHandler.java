package tech.crm.crmserver.handler;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse, Authentication e) throws
            IOException, ServletException {
        //set status to 200(OK)
        httpServletResponse.setStatus(HttpStatus.OK.value());
        //write json response
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        map.put("status", "OK");
        map.put("msg","successful operation!");
        map.put("data","");
        Gson gson = new Gson();
        httpServletResponse.getWriter().write(gson.toJson(map));
    }
}
