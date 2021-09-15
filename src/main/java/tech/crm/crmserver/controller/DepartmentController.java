package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/department")
public class DepartmentController {


    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserService userService;

    @GetMapping("/member")
    public ResponseResult<Object> test(@RequestParam("department_id") Integer departmentId,
                                       @RequestParam("size") Integer size,
                                       @RequestParam("current") Integer current){
        Permission userPermission = permissionService.findPermission(departmentId, userService.getId());
        if(userPermission == null){
            return ResponseResult.fail("You are not a member of this department", HttpStatus.FORBIDDEN);
        }
        Page<UserPermissionDTO> p = permissionService.getAllPermissionInDepartmentOrdered(new Page<>(current, size), departmentId);
        return ResponseResult.suc("Success",p);
    }

}

