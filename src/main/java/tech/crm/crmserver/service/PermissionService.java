package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import tech.crm.crmserver.dao.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dto.UserPermissionDTO;

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
     * create permission level for user
     * @param departmentId the department id of the department user want to join
     * @param userId the user who wants to join the department
     * @return ture if create permission successfully
     */
    public boolean createPermission(Integer departmentId, Integer userId,Integer permissionLevel);

    public boolean updateOrCreatePermission(Integer departmentId, Integer executor,Integer executed, Integer permissionLevel);

    public Permission findPermission(Integer departmentId, Integer userId);

    public boolean checkPendingPermission(Integer organizationId, Integer departmentId, Integer userId);

    public Page<UserPermissionDTO> getAllPermissionInDepartmentOrdered(Page<UserPermissionDTO> page, Integer departmentId);
}
