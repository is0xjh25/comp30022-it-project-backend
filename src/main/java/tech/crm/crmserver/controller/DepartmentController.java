package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.exception.UserNotInDepartmentException;
import tech.crm.crmserver.service.DepartmentService;
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
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    /**
     * find all the member in the department<br/>
     * will check whether the user has the permission(user need to be an official member in the department)
     * @param departmentId which department user want to search
     * @param size size of each page
     * @param current current page
     * @return all the member within the department
     */
    @GetMapping("/member")
    public ResponseResult<Object> findMember(@RequestParam("department_id") Integer departmentId,
                                       @RequestParam("size") Integer size,
                                       @RequestParam("current") Integer current){
        //check whether the user is in this department
        Permission userPermission = permissionService.findPermission(departmentId, userService.getId());
        if(userPermission == null || userPermission.getAuthorityLevel().equal(PermissionLevel.PENDING)){
            throw new UserNotInDepartmentException(HttpStatus.FORBIDDEN);
        }
        //find all the member within the department
        Page<UserPermissionDTO> p = permissionService.getAllPermissionInDepartmentOrdered(new Page<>(current, size), departmentId);
        return ResponseResult.suc("Success",p);
    }

    /**
     * create permission(default permission level is PENDING)
     * which means the user apply to join the department
     * @param departmentId which department user want to join
     * @return whether the user successfully commit the apply
     */
    @PostMapping("/join")
    public ResponseResult<Object> createPermission(@RequestParam("department_id") Integer departmentId,
                                                   @RequestParam(value = "permission_level",
                                                           defaultValue = "0") Integer permissionLevel){
        if(permissionLevel != 0){
            throw new NotEnoughPermissionException();
        }
        permissionService.createPermission(departmentId, userService.getId(),permissionLevel);
        return ResponseResult.suc("Successfully create permission!");
    }

    /**
     * delete the department by department id<br/>
     * will delete the related entities(permissions, contacts)<br/>
     * will check the permission<br/>
     * @param departmentId the id of department needed to be deleted
     * @return whether the user successfully commit the apply
     */
    @DeleteMapping
    public ResponseResult<Object> deleteDepartment(@RequestParam("department_id") Integer departmentId){
        departmentService.deleteDepartmentByDepartmentId(departmentId);
        return ResponseResult.suc("Successfully delete the department!");
    }

    /**
     * search member in department
     * @param departmentId the id of department
     * @param searchKey search key
     * @param size the size of the page
     * @param current the value of current page
     * @return ResponseResult with msg
     */
    @GetMapping("/searchMember")
    public ResponseResult<Object> searchMemberInOrganization(@RequestParam("department_id") Integer departmentId,
                                                             @RequestParam("search_key") String searchKey,
                                                             @RequestParam("size") Integer size,
                                                             @RequestParam("current") Integer current){
        //check permission
        if(!permissionService.ifPermissionLevelSatisfied(userService.getId(),PermissionLevel.DISPLAY,departmentId)){
            throw new NotEnoughPermissionException();
        }
        Page<UserPermissionDTO> userPermissionDTOPage = departmentService.searchMember(new Page<>(current, size), departmentId, searchKey);
        return ResponseResult.suc("success",userPermissionDTOPage);
    }

}

