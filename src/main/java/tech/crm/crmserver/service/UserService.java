package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.User;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserDTO;

/**
 * <p>
 *  service for User
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface UserService extends IService<User> {

    /**
     * Verify whether the email and password is correct
     *
     * @param loginRequest login form for login request
     * @return login user
     */
    public User verify(LoginRequest loginRequest);

    /**
     * Login and return the token
     * @param loginRequest login form for login request
     * @return Token
     */
    public String login(LoginRequest loginRequest);

    /**
     * Register the user
     *
     * @param user user need for register
     * @return null if fail, return user when successfully register
     */
    public User register(User user);

    /**
     * Transfer a userDTO to user class
     *
     * @param userDTO userDTO class
     * @return user class
     */
    public User fromUserDTO(UserDTO userDTO);

    /**
     * Check whether the encoded of current Password matches the password
     *
     * @param currentPassword raw Password
     * @param password encodedPassword
     * @return whether the password matches
     */
    public boolean check(String currentPassword, String password);

    /**
     * Get user Id from SecurityContextHolder
     * the Id was stored in the SecurityContextHolder at tech/crm/crmserver/security/JwtAuthorizationFilter.java
     *
     * @return user id
     */
    public Integer getId();

    /**
     * generate a random password and store encrypted password into database <br/>
     * then send the password to the email
     * @param email the email of user who needs reset password
     */
    public void resetPassword(String email);
}
