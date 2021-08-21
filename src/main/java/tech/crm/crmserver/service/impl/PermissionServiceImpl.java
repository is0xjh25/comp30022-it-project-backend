package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.mapper.PermissionMapper;
import tech.crm.crmserver.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-21
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

}
