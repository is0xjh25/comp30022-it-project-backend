package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.DepartmentDTO;
import tech.crm.crmserver.dto.UserPermissionDTO;

import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface PermissionService extends IService<Permission> {


    /**
     * Create a permission based on department id, user id, permission level
     *
     * @param departmentId the id of the department to create permission
     * @param userId the id of the user
     * @param permissionLevel the permission level of permission to create
     * @return if create success
     */
    public boolean createPermission(Integer departmentId, Integer userId,Integer permissionLevel);

    /**
     * Update a permission based on department id, executor id, executed id and permission level
     *
     * @param departmentId the id of the department to update permission
     * @param executor the user id of the executor of updating
     * @param executed the user id of the executed of updating
     * @param permissionLevel the permission level of permission to update
     * @return if updating success
     */
    public boolean updateOrCreatePermission(Integer departmentId, Integer executor,Integer executed, Integer permissionLevel);

    /**
     * Query permissions based on department id, user id
     *
     * @param departmentId the id of the department to match
     * @param userId the user id to match
     * @return a match permission
     */
    public Permission findPermission(Integer departmentId, Integer userId);

    /**
     * Check if there is pending permission request based on organization id and department id
     *
     * @param organizationId the id of the organization to check
     * @param departmentId the id of the department to check
     * @param userId the user id to check
     * @return a boolean represent if there is pending permission request
     */
    public boolean checkPendingPermission(Integer organizationId, Integer departmentId, Integer userId);

    /**
     * Get all the permission based on departmentId
     *
     * @param page the page configuration, including the page size, current page
     * @param departmentId the id of the department to get permission
     * @return a page of UserPermissionDTO including all the match permission data
     */
    public Page<UserPermissionDTO> getAllPermissionInDepartmentOrdered(Page<UserPermissionDTO> page, Integer departmentId);

    /**
     * Get all the permission based on userId and permission level
     *
     * @param userId the userId to match
     * @param permissionLevel the permission level to match
     * @return a list of match permission
     */
    public List<Permission> getPermissionByUserId(Integer userId, PermissionLevel permissionLevel);

    /**
     * Get all the permission based on userId
     *
     * @param userId the userId to match
     * @return a list of match permission
     */
    public List<Permission> getPermissionByUserId(Integer userId);

    /**
     * Get the ownership status based on permissionLevel and departmentId
     *
     * @param permissionList all the permission
     * @param departmentId the departmentId of the permission need to find
     * @return a status about about the ownership status
     */
    public DepartmentDTO.Status getDepartmentOwnerShipStatus(List<Permission> permissionList, Integer departmentId);

    /**
     * Delete permission of the department
     *
     * @param departmentId the id of department
     */
    public void deletePermissionByDepartmentId(Integer departmentId);

    /**
     * Delete permission of the department
     *
     * @param departmentIdList the list of ids of department
     */
    public void deletePermissionByDepartmentIdList(List<Integer> departmentIdList);
}
