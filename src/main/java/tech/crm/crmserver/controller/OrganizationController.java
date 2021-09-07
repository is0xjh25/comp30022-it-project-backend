package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dto.OrganizationDTO;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.UserService;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.OrganizationService;
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
@RequestMapping("/organization")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @PostMapping("/department")
    public ResponseResult<Object> createDepartment(@RequestParam("organization_id") Integer org_id,
                                           @RequestParam("department_name") String name){
        Organization organization = null;
        try{
            organization = organizationService.getById(org_id);
        }
        catch (Exception e){
            return ResponseResult.fail("Fail to get organization from database(Maybe Organization do not exist)");
        }
        //organization not exist
        if(organization == null){
            return ResponseResult.fail("Organization do not exist");
        }
        //check the authority(creator should be the owner of the organization)
        if(organization.getOwner() != userService.getId()){
            return ResponseResult.fail("You don't have enough permission.",HttpStatus.UNAUTHORIZED);
        }
        Department department = new Department();
        department.setOrganizationId(org_id);
        department.setName(name);
        try {
            departmentService.save(department);
        }
        catch (Exception e){
            return ResponseResult.fail("Department already exist.(Or database error)");
        }
        return ResponseResult.suc("Successfully create department!");
    }


    /**
     * Get departments based on organization id
     * */
    @GetMapping("/department")
    public ResponseResult<Object> getDepartment(@RequestParam("organization_id") Integer organization_id){
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("organization_id",organization_id);
        List<Department> departments = departmentService.list(wrapper);
        return ResponseResult.suc("success", departments);
    }

    // Check out all organization this user belongs to
    @GetMapping("/myOrganization")
    public ResponseResult<Object> getAllOrganization() {
        Integer userId = userService.getId();
        List<Organization> organizations = organizationService.getAllOrgUserOwnAndBelongTo(userId);
        return ResponseResult.suc("success", organizations);
    }

    /**
     * Get Organization based on organization Integer
     * */
    @GetMapping()
    public ResponseResult<Object> getOrganization(@RequestParam("organization_id") Integer organizationId) {
        Organization organization = organizationService.getById(organizationId);
        return ResponseResult.suc("success", organization);
    }

    /**
     * Get Organization based on organization Integer
     * */
    @GetMapping("/name")
    public ResponseResult<Object> getOrganizationBasedOnName(@RequestParam String organizationName) {
        List<Organization> organizations = organizationService.getOrgBasedOnName(organizationName);
        return ResponseResult.suc("success", organizations);
    }

    /**
     * Create new Organization
     * */
    @PostMapping()
    public ResponseEntity<Void> createNewOrganization(@RequestParam String organizationName) {
        Integer userID = userService.getId();
        Organization newOrganization = new Organization();
        newOrganization.setName(organizationName);
        newOrganization.setId(userID);
        try {
            organizationService.save(newOrganization);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

