package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Permission;
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
        wrapper.eq("user_id",userService.getId());
        wrapper.eq("department_id",departmentId);
        Permission executor = permissionService.getOne(wrapper);
        //executed person for delete action
        wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        wrapper.eq("department_id",departmentId);
        Permission executed = permissionService.getOne(wrapper);

        //check the authority
        if(executor.getAuthorityLevel().getLevel() >= PermissionLevel.MANAGE.getLevel() &&
                executor.getAuthorityLevel().getLevel() > executed.getAuthorityLevel().getLevel()){
            permissionService.removeById(executed.getId());
        }
        return ResponseResult.suc("Successfully delete the member!");
    }

    /**
     * member apply for joining a department
     * @param departmentId
     * @return
     */
    @PostMapping("/pending")
    public ResponseResult<Object> joinDepartment(@RequestParam("department_id") Integer departmentId){
        permissionService.applyPendingPermission(departmentId, userService.getId());
        return ResponseResult.suc("Successfully apply for permission!");
    }

}

