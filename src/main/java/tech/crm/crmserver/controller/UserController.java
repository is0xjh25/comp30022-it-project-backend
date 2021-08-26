package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;


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
    private TokenKeyService tokenKeyService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest){
        String token = tokenKeyService.createToken(loginRequest);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, token);
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        tokenKeyService.removeToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public String register(){
        return "register";
    }

    @GetMapping
    public User getUser(){
        Integer id = userService.getId();
        if (id != null) {
            return userService.getById(id);
        }
        return null;
    }



}

