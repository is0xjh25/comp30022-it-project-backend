package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.mapper.PermissionMapper;
import tech.crm.crmserver.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.UserService;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public boolean applyPendingPermission(Integer departmentId, Integer userId) {
        //check whether this user in the department
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId);
        wrapper.eq("user_id",userId);
        if(permissionMapper.selectOne(wrapper) != null){
            log.warn("This user already in the department");
        }
        Permission permission = new Permission();
        permission.setDepartmentId(departmentId);
        permission.setUserId(userId);
        permission.setAuthorityLevel(PermissionLevel.PENDING);
        permissionMapper.insert(permission);
        return true;
    }

}
