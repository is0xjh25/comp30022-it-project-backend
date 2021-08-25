package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.security.PersistentTokenBasedRememberMeServicesImpl;

import javax.servlet.http.HttpServletRequest;


/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public PersistentTokenBasedRememberMeServicesImpl rememberMeServices;

    @PostMapping
    public String register(){
        return "register";
    }

    @GetMapping
    public User getUserByToken(HttpServletRequest request){
        return rememberMeServices.gotUserFromRequest(request);
    }

}

