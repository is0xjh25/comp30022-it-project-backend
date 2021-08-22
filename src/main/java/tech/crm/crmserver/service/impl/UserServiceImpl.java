package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.mapper.UserMapper;
import tech.crm.crmserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
