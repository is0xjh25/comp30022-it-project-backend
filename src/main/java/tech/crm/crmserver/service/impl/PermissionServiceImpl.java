package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.DepartmentDTO;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.exception.UserAlreadyInDepartmentException;
import tech.crm.crmserver.exception.UserNotInDepartmentException;
import tech.crm.crmserver.mapper.PermissionMapper;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.PermissionService;
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

    @Autowired
    private UserService userService;

    /**
     * Create a permission based on department id, user id, permission level
     *
     * @param departmentId the id of the department to create permission
     * @param userId the id of the user
     * @param permissionLevel the permission level of permission to create
     * @return if create success
     */
    @Override
    public boolean createPermission(Integer departmentId, Integer userId, Integer permissionLevel) {
        if(findPermission(departmentId,userId) != null){
            log.warn("This user already in the department");
            throw new UserAlreadyInDepartmentException();
        }
        Permission permission = new Permission();
        permission.setDepartmentId(departmentId);
        permission.setUserId(userId);
        permission.setAuthorityLevel(PermissionLevel.getPermissionLevel(permissionLevel));
        permissionMapper.insert(permission);
        return true;
    }

    /**
     * Update a permission based on department id, executor id, executed id and permission level
     *
     * @param departmentId the id of the department to update permission
     * @param executor the user id of the executor of updating
     * @param executed the user id of the executed of updating
     * @param permissionLevel the permission level of permission to update
     * @return if updating success
     */
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
            throw new NotEnoughPermissionException();
        }
        if(executorPermission.getAuthorityLevel().getLevel() < PermissionLevel.MANAGE.getLevel()
        || executorPermission.getAuthorityLevel().getLevel() <= permissionLevel
        || executorPermission.getAuthorityLevel().getLevel() < executedPermission.getAuthorityLevel().getLevel()){
            log.warn("Do not have enough permission!");
            throw new NotEnoughPermissionException();
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

    /**
     * Query permissions based on department id, user id
     *
     * @param departmentId the id of the department to match
     * @param userId the user id to match
     * @return a match permission
     */
    @Override
    public Permission findPermission(Integer departmentId, Integer userId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId)
                .eq("user_id",userId);
        Permission permission = permissionMapper.selectOne(wrapper);
        return permission;
    }

    /**
     * Check if there is pending permission request based on organization id and department id
     *
     * @param organizationId the id of the organization to check
     * @param departmentId the id of the department to check
     * @param userId the user id to check
     * @return a boolean represent if there is pending permission request
     */
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

    /**
     * Get all the permission based on departmentId
     *
     * @param page the page configuration, including the page size, current page
     * @param departmentId the id of the department to get permission
     * @return a page of UserPermissionDTO including all the match permission data
     */
    @Override
    public Page<UserPermissionDTO> getAllPermissionInDepartmentOrdered(Page<UserPermissionDTO> page, Integer departmentId) {
        return this.baseMapper.getPermissionInDepartmentOrdered(page,departmentId);
    }

    /**
     * Get all the permission based on userId and permission level
     *
     * @param userId the userId to match
     * @param permissionLevel the permission level to match
     * @return a list of match permission
     */
    @Override
    public List<Permission> getPermissionByUserId(Integer userId, PermissionLevel permissionLevel) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ge("authority_level", permissionLevel.getLevel());
        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);
        return permissionList;
    }

    /**
     * Get all the permission based on userId
     *
     * @param userId the userId to match
     * @return a list of match permission
     */
    @Override
    public List<Permission> getPermissionByUserId(Integer userId) {
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);
        return permissionList;
    }

    /**
     * Get the ownership status based on permissionLevel and departmentId
     *
     * @param permissionList all the permission
     * @param departmentId the departmentId of the permission need to find
     * @return a status about about the ownership status
     */
    @Override
    public DepartmentDTO.Status getDepartmentOwnerShipStatus(List<Permission> permissionList, Integer departmentId) {
        for (Permission permission : permissionList) {
            if (permission.getDepartmentId().equals(departmentId)) {
                if (permission.getAuthorityLevel().equal(PermissionLevel.OWNER)) {
                    return DepartmentDTO.Status.OWNER;
                } else if (permission.getAuthorityLevel().equal(PermissionLevel.DISPLAY) ||
                        permission.getAuthorityLevel().equal(PermissionLevel.UPDATE) ||
                        permission.getAuthorityLevel().equal(PermissionLevel.DELETE) ||
                        permission.getAuthorityLevel().equal(PermissionLevel.MANAGE)) {
                    return DepartmentDTO.Status.MEMBER;
                }
            }
        }
        return DepartmentDTO.Status.NOT_JOIN;
    }

    /**
     * Delete permission of the department
     *
     * @param departmentId the id of department
     */
    @Override
    public void deletePermissionByDepartmentId(Integer departmentId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id",departmentId);
        baseMapper.delete(wrapper);
    }

    /**
     * Delete permission of the department
     *
     * @param departmentIdList the list of ids of department
     */
    @Override
    public void deletePermissionByDepartmentIdList(List<Integer> departmentIdList) {
        if(departmentIdList.isEmpty()){
            return;
        }
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.in("department_id",departmentIdList);
        baseMapper.delete(wrapper);
    }

    /**
     * Delete a member from a department
     *
     * @param userId       the user id to delete
     * @param departmentId the departmentId to delete
     */
    @Override
    public void deleteMember(Integer userId, Integer departmentId) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        //executor for delete action
        wrapper.eq("user_id",userService.getId())
                .eq("department_id",departmentId);
        Permission executor = getOne(wrapper);
        if(executor == null){
            throw new UserNotInDepartmentException(HttpStatus.FORBIDDEN);
        }
        //executed person for delete action
        wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId)
                .eq("department_id",departmentId);
        Permission executed = getOne(wrapper);
        if(executed == null){
            throw new UserNotInDepartmentException();
        }
        //check the authority
        if(executor.getAuthorityLevel().getLevel() >= PermissionLevel.MANAGE.getLevel() &&
                executor.getAuthorityLevel().getLevel() > executed.getAuthorityLevel().getLevel()){
            removeById(executed.getId());
        }
        else {
            throw new NotEnoughPermissionException();
        }
    }

    /**
     * Get a user's permission in a department
     *
     * @param userId       the user id to delete
     * @param departmentId the departmentId to delete
     */
    @Override
    public Permission getPermissionByUserIdAndDepartmentId(Integer userId, Integer departmentId) {
        List<Permission> permissionByUserId = getPermissionByUserId(userId);
        for (Permission permission : permissionByUserId) {
            if (permission.getDepartmentId().equals(departmentId)) {
                return permission;
            }
        }
        return null;
    }
}
