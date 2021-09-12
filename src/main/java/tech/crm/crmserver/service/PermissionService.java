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


    /**
     * use when user want to join a department
     * permission level is set to PermissionLevel.PENDING
     * @param departmentId the department id of the department user want to join
     * @param userId the user who wants to join the department
     * @return ture if apply pending permission successfully
     */
    public boolean applyPendingPermission(Integer departmentId, Integer userId);

}
