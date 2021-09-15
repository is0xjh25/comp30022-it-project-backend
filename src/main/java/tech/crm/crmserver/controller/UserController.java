package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.common.response.Result;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserDTO;
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
    public ResponseResult<Object> login(@RequestBody LoginRequest loginRequest){
        //verify the user
        User user = null;
        try{
            user = userService.verify(loginRequest);
        }
        catch (BadCredentialsException e){
            return ResponseResult.fail("The user name or password is not correct.");
        }
        if(user == null){
            return ResponseResult.fail("The user name or password is not correct.");
        }
        //return the token
        String token = tokenKeyService.createToken(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, token);
        return ResponseResult.suc("successfully login!","",httpHeaders);
    }

    @PostMapping("/logout")
    public ResponseResult<Object> logout(@RequestHeader("Authorization") String token) {
        tokenKeyService.removeToken(token);
        return ResponseResult.suc("successfully logout!");
    }

    @PostMapping
    public ResponseResult<Object> register(@RequestBody UserDTO userDTO){
        User user = userService.fromUserDTO(userDTO);
        //check whether there is same email already exist
        if(userService.register(user) == null){
            return ResponseResult.fail("Same email already exist!");
        }
        //return token
        String token = tokenKeyService.createToken(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, token);
        return ResponseResult.suc("successfully sign up!","",httpHeaders);
    }

    @GetMapping
    public ResponseResult<Object> getUser() {
        Integer id = userService.getId();
        if (id != null) {
            User user = userService.getById(id);
            user.setPassword(null);
            return ResponseResult.suc("successfully get user", user);
        }
        return ResponseResult.fail("Error");
    }


}

