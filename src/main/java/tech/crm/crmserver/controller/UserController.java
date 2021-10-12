package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.common.exception.BadPhotoException;
import tech.crm.crmserver.service.TokenKeyService;
import tech.crm.crmserver.service.UserService;

import java.io.IOException;


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
        //login and return the token
        String token = userService.login(loginRequest);
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
     * @param userRegisterDTO the form for register, contain all the information required for User
     * @return 200 when successfully register and set Authorization in response header
     */
    @PostMapping
    public ResponseResult<Object> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO){
        User user = userService.fromUserRegisterDTO(userRegisterDTO);
        //check whether there is same email already exist
        userService.register(user);
        return ResponseResult.suc("successfully sign up!");
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
            UserDTO userDTO = new UserDTO();
            NullAwareBeanUtilsBean.copyProperties(user,userDTO);
            return ResponseResult.suc("successfully get user", userDTO);
        }
        return ResponseResult.fail("Error");
    }

    /**
     * Send the new password to user's email<br/>
     * ,store the new encoded password into database<br/>
     * and delete all the token of this user in database
     * @param resetPasswordDTO the form of resetPassword
     * @return response with msg
     */
    @PostMapping("/resetPassword")
    public ResponseResult<Object> resetPassword(@Validated @RequestBody ResetPasswordDTO resetPasswordDTO){
        userService.resetPassword(resetPasswordDTO.getEmail());
        return ResponseResult.suc("Check your email for new password!");
    }

    /**
     * Update user details
     *
     * @param userUpdateDTO the form of update user details
     * @return response with msg
     */
    @PutMapping
    public ResponseResult<Object> updateUserDetail(@Validated @RequestBody UserUpdateDTO userUpdateDTO){
        userService.updateUser(userUpdateDTO);
        return ResponseResult.suc("Successfully update user detail");
    }

    @PostMapping("/verify")
    public ResponseResult<Object> verifyEmail(){
        userService.activePendingUser(userService.getId());
        return ResponseResult.suc("Successfully active user account");
    }

    @PostMapping("/uploadPhoto")
    public ResponseResult<Object> uploadPhoto(@RequestParam("photo") MultipartFile photo) throws IOException {
        if(photo == null){
            throw new BadPhotoException();
        }
        userService.updatePhoto(userService.getId(), photo);
        return ResponseResult.suc("Successfully upload the file");
    }

}

