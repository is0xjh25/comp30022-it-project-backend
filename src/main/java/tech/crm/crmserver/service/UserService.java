package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.User;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.LoginRequest;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface UserService extends IService<User> {

    public boolean check(String currentPassword, String password);

    public Integer getId();

}
