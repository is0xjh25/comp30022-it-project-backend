package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.User;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.LoginRequest;
import tech.crm.crmserver.dto.UserDTO;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface UserService extends IService<User> {

    public User verify(LoginRequest loginRequest);

    public User register(User user);

    public User fromUserDTO(UserDTO userDTO);

    public boolean check(String currentPassword, String password);

    public Integer getId();

}
