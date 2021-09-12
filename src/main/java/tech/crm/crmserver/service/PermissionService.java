package tech.crm.crmserver.service;

import tech.crm.crmserver.dao.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface PermissionService extends IService<Permission> {

    public boolean checkPendingPermission(Integer organizationId, Integer departmentId);

    public boolean checkPendingPermission(Integer departmentId);

    public boolean getAllPermissionInDepartmentOrdered(Integer departmentId);
}
