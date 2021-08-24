package tech.crm.crmserver.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping
    public String register(){
        return "register";
    }

    @GetMapping
    public String getUserByToken(){
        return "User";
    }

}

