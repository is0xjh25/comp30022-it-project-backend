package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserDTO;
import tech.crm.crmserver.exception.LoginBadCredentialsException;
import tech.crm.crmserver.exception.UserAlreadyExistException;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;


/**
 * <p>
 *  controller for user API
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

    /**
     * The login  API
     *
     * @param loginRequest the form for login
     * @return return 200 when login successfully, return 400 and reason in the msg
     */
    @PostMapping("/login")
    public ResponseResult<Object> login(@Validated @RequestBody LoginRequest loginRequest){
        //verify the user
        User user = userService.verify(loginRequest);
        //return the token
        String token = tokenKeyService.createToken(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, token);
        return ResponseResult.suc("successfully login!","",httpHeaders);
    }

    /**
     * Using the token to logout, will remove the token from the database
     *
     * @param token the token need to removed
     * @return return successfully logout
     */
    @PostMapping("/logout")
    public ResponseResult<Object> logout(@RequestHeader("Authorization") String token) {
        tokenKeyService.removeToken(token);
        return ResponseResult.suc("successfully logout!");
    }

    /**
     * Register API
     *
     * @param userDTO the form for register, contain all the information required for User
     * @return 200 when successfully register and set Authorization in response header
     */
    @PostMapping
    public ResponseResult<Object> register(@Validated @RequestBody UserDTO userDTO){
        User user = userService.fromUserDTO(userDTO);
        //check whether there is same email already exist
        userService.register(user);
        //return token
        String token = tokenKeyService.createToken(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(SecurityConstants.TOKEN_HEADER, token);
        return ResponseResult.suc("successfully sign up!","",httpHeaders);
    }

    /**
     * Will set password to null
     * Since the Token contains the User id, thus, we can use this to get the id in the Token and
     * return the user
     *
     * @return the user, if something go wrong, return Error
     */
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

