package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.dto.UserRegisterDTO;
import tech.crm.crmserver.dto.UserUpdateDTO;

import java.util.HashMap;
import java.util.Map;

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
     * return the search map for search User in database
     * @param searchKey search key
     * @return search map
     */
    public static Map<String, String> searchKey(String searchKey){
        if(searchKey == null){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        map.put("email",searchKey);
        map.put("first_name",searchKey);
        map.put("last_name",searchKey);
        map.put("middle_name",searchKey);
        return map;
    }

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
     * Transfer a UserRegisterDTO to user class
     *
     * @param userRegisterDTO userDTO class
     * @return user class
     */
    public User fromUserRegisterDTO(UserRegisterDTO userRegisterDTO);

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
     * then send the password to the email<br/>
     * and delete all the token of this user in database
     * @param email the email of user who needs reset password
     */
    public void resetPassword(String email);

    /**
     * update the recent activity time of user
     * @param userId user id
     */
    public void updateRecentActivity(Integer userId);

    /**
     * update the not null properties in userUpdateDTO
     * @param userUpdateDTO user update information
     */
    public void updateUser(UserUpdateDTO userUpdateDTO);

    /**
     * check whether the user status is pending<br/>
     * if the status is pending, resend verify email and throw error
     * @param user user whose status is pending
     */
    public void dealPendingUser(User user);

    /**
     * active the user
     * @param userId user id of user need to active
     */
    public void activePendingUser(Integer userId);

    /**
     * return page of UserPermissionDTO by wrapper from permission
     * @param page page config
     * @param queryWrapper wrapper
     * @return return page of UserPermissionDTO by wrapper
     */
    public Page<UserPermissionDTO> getUserPermissionDTO(Page<?> page, Wrapper<User> queryWrapper);

    /**
     * return page of UserPermissionDTO by wrapper from belong to
     * @param page page config
     * @param queryWrapper wrapper
     * @return return page of UserPermissionDTO by wrapper
     */
    public Page<UserPermissionDTO> getUserPermissionDTOInOrganization(Page<?> page, Wrapper<User> queryWrapper);
}
