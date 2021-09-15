package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

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

    /**
     * create permission(default permission level is PENDING)
     * @param departmentId
     * @return
     */
    @PostMapping
    public ResponseResult<Object> createPermission(@RequestParam("department_id") Integer departmentId,
                                                 @RequestParam(value = "permission_level",
                                                         defaultValue = "0") Integer permissionLevel){
        if(permissionLevel != 0){
            return ResponseResult.fail("You don't have enough permission!", HttpStatus.FORBIDDEN);
        }
        permissionService.createPermission(departmentId, userService.getId(),permissionLevel);
        return ResponseResult.suc("Successfully create permission!");
    }

    @PutMapping
    public ResponseResult<Object> updatePermission(@RequestParam("department_id") Integer departmentID,
                                                   @RequestParam("member id") Integer memberId,
                                                   @RequestParam(value = "permission_level")Integer permissionLevel){
        if(permissionService.updateOrCreatePermission(departmentID,userService.getId(),memberId,permissionLevel)){
            return ResponseResult.suc("Successfully update permission!");
        }
        return ResponseResult.fail("You don't have enough permission!", HttpStatus.FORBIDDEN);
    }

    @GetMapping("/pending")
    public ResponseResult<Object> getIfOrgDepartmentHasPendingRequest
            (@RequestParam(value = "organization_id", required = false) Integer organizationId,
             @RequestParam(value = "department_id", required = false) Integer departmentId) {
        if (permissionService.checkPendingPermission(organizationId, departmentId)) {
            return ResponseResult.suc("Have pending");
        }
        return ResponseResult.suc("No pending");
    }



}

