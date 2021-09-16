package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.UserPermissionDTO;
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
     * delete a member from a department
     * @return
     */
    @DeleteMapping
    public ResponseResult<Object> deleteMember(@RequestParam("user_id") Integer userId,
                                               @RequestParam("department_id") Integer departmentId){
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        //executor for delete action
        wrapper.eq("user_id",userService.getId())
                .eq("department_id",departmentId);
        Permission executor = permissionService.getOne(wrapper);
        //executed person for delete action
        wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId)
                .eq("department_id",departmentId);
        Permission executed = permissionService.getOne(wrapper);

        //check the authority
        if(executor.getAuthorityLevel().getLevel() >= PermissionLevel.MANAGE.getLevel() &&
                executor.getAuthorityLevel().getLevel() > executed.getAuthorityLevel().getLevel()){
            permissionService.removeById(executed.getId());
        }
        return ResponseResult.suc("Successfully delete the member!");
    }

    @PutMapping
    public ResponseResult<Object> updatePermission(@RequestParam("department") Integer departmentID,
                                                   @RequestParam("member") Integer memberId,
                                                   @RequestParam(value = "permission")Integer permissionLevel){
        if(permissionService.updateOrCreatePermission(departmentID,userService.getId(),memberId,permissionLevel)){
            return ResponseResult.suc("Successfully update permission!");
        }
        return ResponseResult.fail("You don't have enough permission!", HttpStatus.FORBIDDEN);
    }

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

        if (isOwnOrganization) {
            if (permissionService.checkPendingPermission(organizationId, departmentId, userId)) {
                return ResponseResult.suc("Have pending");
            }
        }
        return ResponseResult.suc("No pending");
    }

}

