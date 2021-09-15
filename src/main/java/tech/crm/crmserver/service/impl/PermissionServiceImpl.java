package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.BelongTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.mapper.OrganizationMapper;
import tech.crm.crmserver.mapper.PermissionMapper;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.PermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.service.UserService;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    public DepartmentService departmentService;

    @Autowired
    public OrganizationService organizationService;

    @Override
    public boolean createPermission(Integer departmentId, Integer userId, Integer permissionLevel) {
        if(findPermission(departmentId,userId) != null){
            log.warn("This user already in the department");
        }
        Permission permission = new Permission();
        permission.setDepartmentId(departmentId);
        permission.setUserId(userId);
        permission.setAuthorityLevel(PermissionLevel.getPermissionLevel(permissionLevel));
        permissionMapper.insert(permission);
        return true;
    }

    @Override
    public boolean updateOrCreatePermission(Integer departmentId, Integer executor, Integer executed, Integer permissionLevel) {
        //whether the executed member is in the department
        boolean existence = true;
        //find the permission for executed and executor
        Permission executedPermission = findPermission(departmentId,executed);
        Permission executorPermission = findPermission(departmentId,executor);
        if(executedPermission == null){
            log.warn("This member not in the department");
            existence = false;
            executedPermission = new Permission();
            executedPermission.setDepartmentId(departmentId);
            executedPermission.setUserId(executed);
            executedPermission.setAuthorityLevel(PermissionLevel.PENDING);
        }
        if(executorPermission == null){
            log.warn("This user not in the department");
            return false;
        }
        if(executorPermission.getAuthorityLevel().getLevel() < PermissionLevel.MANAGE.getLevel()
        || executorPermission.getAuthorityLevel().getLevel() <= permissionLevel
        || executorPermission.getAuthorityLevel().getLevel() > executedPermission.getAuthorityLevel().getLevel()){
            log.warn("Do not have enough permission!");
            return false;
        }
        executedPermission.setAuthorityLevel(PermissionLevel.getPermissionLevel(permissionLevel));
        if (existence) {
            permissionMapper.updateById(executedPermission);
        }
        else {
            permissionMapper.insert(executedPermission);
        }
        return true;
    }

    @Override
    public Permission findPermission(Integer departmentId, Integer userId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId)
                .eq("user_id",userId);
        Permission permission = permissionMapper.selectOne(wrapper);
        return permission;
    }

    @Override
    public boolean checkPendingPermission(Integer organizationId, Integer departmentId, Integer userId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();

        List<Integer> departmentIdList = new ArrayList<>();
        if (departmentId != null) {
            departmentIdList.add(departmentId);
        } else if (organizationId != null) {
            departmentIdList.addAll(departmentService.getDepartmentIdByOrganization(organizationId));
        } else {
            List<Organization> organizationList = organizationService.getAllOrgUserOwn(userId);
            for (Organization organization : organizationList) {
                departmentIdList.addAll(departmentService.getDepartmentIdByOrganization(organization.getId()));
            }
        }


        for (Integer department : departmentIdList) {
            queryWrapper.or().eq("department_id", department);
        }
        queryWrapper.eq("authority_level", PermissionLevel.PENDING);
        return permissionMapper.selectList(queryWrapper).size() > 0;
    }

    @Override
    public Page<UserPermissionDTO> getAllPermissionInDepartmentOrdered(Page<UserPermissionDTO> page, Integer departmentId) {
        return this.baseMapper.getPermissionInDepartmentOrdered(page,departmentId);
    }

    @Override
    public List<Permission> getPermissionByUserId(Integer userId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);
        return permissionList;
    }
}
