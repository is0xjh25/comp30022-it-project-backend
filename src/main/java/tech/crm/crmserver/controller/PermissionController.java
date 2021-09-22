package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.exception.UserNotInDepartmentException;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

import java.util.List;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    /**
     * Delete a member from a department
     *
     * @param userId the user id to delete
     * @param departmentId the departmentId to delete
     * @return the ResponseResult with msg,
     */
    @DeleteMapping
    public ResponseResult<Object> deleteMember(@RequestParam("user_id") Integer userId,
                                               @RequestParam("department_id") Integer departmentId){
        permissionService.deleteMember(userId,departmentId);
        return ResponseResult.suc("Successfully delete the member!");

    }

    /**
     * Update the permission of a member in a department
     *
     * @param departmentId departmentId to update permission level
     * @param userId the member to update
     * @param permissionLevel the new permission level to update
     * @return ResponseResult with msg
     */
    @PutMapping
    public ResponseResult<Object> updatePermission(@RequestParam("department_id") Integer departmentId,
                                                   @RequestParam("user_id") Integer userId,
                                                   @RequestParam(value = "permission_level")Integer permissionLevel){
        permissionService.updateOrCreatePermission(departmentId,userService.getId(), userId,permissionLevel);
        return ResponseResult.suc("Successfully update permission!");
    }


    /**
     * Check if there is a pending join department request, if both
     * organizationId and departmentId is null, and check if the organizations
     * own by this user has pending request
     *
     * @param organizationId organizationId to check
     * @param departmentId departmentId to check
     * @return the ResponseResult with msg, with the boolean value about if the there is pending request
     */
    @GetMapping("/pending")
    public ResponseResult<Object> getIfOrgDepartmentHasPendingRequest
            (@RequestParam(value = "organization_id", required = false) Integer organizationId,
             @RequestParam(value = "department_id", required = false) Integer departmentId) {
        Integer userId = userService.getId();
        List<Organization> organizationList = organizationService.getAllOrgUserOwn(userId);
        boolean isOwnOrganization = false;

        for (Organization organization : organizationList) {
            if (organization.getId().equals(organizationId)) {
                isOwnOrganization = true;
                break;
            }
        }

        if (organizationId == null)  {
            isOwnOrganization = true;
        }

        if (isOwnOrganization) {
            if (permissionService.checkPendingPermission(organizationId, departmentId, userId)) {
                return ResponseResult.suc("Have pending");
            }
        }
        return ResponseResult.suc("No pending");
    }
}

