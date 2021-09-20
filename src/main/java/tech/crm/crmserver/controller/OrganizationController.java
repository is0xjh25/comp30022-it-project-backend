package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.enums.Status;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.DepartmentDTO;
import tech.crm.crmserver.dto.OrganizationDTO;
import tech.crm.crmserver.service.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Organization;
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

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private BelongToService belongToService;

    /**
     * create a department
     * the user need to be the owner of the department
     * @param orgId which organization this department belong to
     * @param name the name of the department
     * @return the ResponseResult with msg
     */
    @PostMapping("/department")
    public ResponseResult<Object> createDepartment(@RequestParam("organization_id") Integer orgId,
                                           @RequestParam("department_name") String name){
        Organization organization = null;
        try{
            organization = organizationService.getById(orgId);
        }
        catch (Exception e){
            return ResponseResult.fail("Fail to get organization from database(Maybe Organization do not exist)");
        }
        //organization not exist
        if(organization == null){
            return ResponseResult.fail("Organization do not exist");
        }
        //check the authority(creator should be the owner of the organization)
        if(!organization.getOwner().equals(userService.getId())){
            return ResponseResult.fail("You don't have enough permission.",HttpStatus.FORBIDDEN);
        }
        Department department = new Department();
        department.setOrganizationId(orgId);
        department.setName(name);
        //check whether there already exist a department with same name
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("organization_id",department.getOrganizationId());
        wrapper.eq("name", department.getName());
        try {
            //department already exist in this organization
            if(departmentService.getOne(wrapper) != null){
                throw new Exception();
            }
            departmentService.save(department);
        }
        catch (Exception e){
            return ResponseResult.fail("Department already exist.(Or database error)");
        }
        //give the owner the owner permission
        department = departmentService.getOne(wrapper);
        Permission permission = new Permission();
        permission.setUserId(userService.getId());
        permission.setDepartmentId(department.getId());
        permission.setAuthorityLevel(PermissionLevel.OWNER);
        permissionService.save(permission);

        return ResponseResult.suc("Successfully create department!");
    }


    /**
     * find all the departments in the organization
     * @param organizationId the id of organization user want to search
     * @return all the departments in the organization
     */
    @GetMapping("/departments")
    public ResponseResult<Object> getDepartment(@RequestParam("organization_id") Integer organizationId){
        Integer userId = userService.getId();
        QueryWrapper<Department> wrapper = new QueryWrapper<>();
        wrapper.eq("organization_id",organizationId);
        //the department should not be deleted
        wrapper.ne("status", Status.DELETED);
        List<Department> departments = departmentService.list(wrapper);
        //change to departmentDTO
        List<DepartmentDTO> response = new ArrayList<>();
        List<Permission> permissionByUserId = permissionService.getPermissionByUserId(userId);
        for (Department department : departments){
            department.setStatus(null);
            DepartmentDTO departmentDTO = new DepartmentDTO();
            NullAwareBeanUtilsBean.copyProperties(department,departmentDTO);
            departmentDTO.setStatus(permissionService.getDepartmentOwnerShipStatus(permissionByUserId, department.getId()).getName());
            response.add(departmentDTO);
        }
        return ResponseResult.suc("success", response);
    }

    /**
     * Check out all organization this user belongs to
     * @return all organization this user belongs to
     */
    @GetMapping("/myOrganization")
    public ResponseResult<Object> getAllOrganization() {
        Integer userId = userService.getId();
        List<Organization> organizations = organizationService.getAllOrgUserOwnAndBelongTo(userId);
        List<OrganizationDTO> organizationDTOS = new ArrayList<>();
        for (Organization organization: organizations) {
            OrganizationDTO organizationDTO = new OrganizationDTO();
            organizationDTO.setId(organization.getId());
            organizationDTO.setOwnerId(organization.getOwner());
            organizationDTO.setName(organization.getName());
            if (organization.getOwner().equals(userId)) {
                organizationDTO.setOwner(true);
            } else {
                organizationDTO.setOwner(false);
            }
            organizationDTOS.add(organizationDTO);
        }
        return ResponseResult.suc("success", organizationDTOS);
    }

    /**
     * Get Organization based on organization Integer
     * @param organizationId the id of organization user want to search
     * @return Organization
     */
    @GetMapping()
    public ResponseResult<Object> getOrganization(@RequestParam("organization_id") Integer organizationId) {
        Organization organization = organizationService.getById(organizationId);
        return ResponseResult.suc("success", organization);
    }

    /**
     * Get Organization based on organization Integer
     * @param organizationName the exact name of organization user want to search
     * @return Organization
     */
    @GetMapping("/name")
    public ResponseResult<Object> getOrganizationBasedOnName(@RequestParam("organization_name") String organizationName) {
        List<Organization> organizations = organizationService.getOrgBasedOnName(organizationName);
        if (organizations.size() == 0) {
            ResponseResult.suc("No match organization");
        }
        return ResponseResult.suc("success", organizations);
    }

    /**
     * Create new Organization
     * @param organizationName the name of organization user want to create
     * @return ResponseResult with mag
     */
    @PostMapping()
    public ResponseResult<Object> createNewOrganization(@RequestParam("organization_name") String organizationName) {
        // todo
        Integer userID = userService.getId();
        List<Organization> organizationListWithSameName = organizationService.getOrgBasedOnExactName(organizationName);

        if (organizationListWithSameName.size() > 0) {
            return ResponseResult.fail("Organization with same name exists");
        }
        Organization newOrganization = new Organization();
        newOrganization.setName(organizationName);
        newOrganization.setOwner(userID);
        try {
            organizationService.save(newOrganization);
        } catch (Exception e) {
            return ResponseResult.fail("Fail to create organization");
        }
        return ResponseResult.suc("success");
    }

    /**
     * delete the organization, belongTo, departments, permission in it<br/>
     * will check the permission
     * @param organizationId the organization id of organization need to be deleted
     * @return ResponseResult with mag
     */
    @DeleteMapping()
    public ResponseResult<Object> deleteOrganization(@RequestParam("organization_id") Integer organizationId){
        organizationService.deleteOrganization(organizationId);
        return ResponseResult.suc("success");
    }

    /**
     * join a organization
     * @param organizationId the id of organization user want to join
     * @return ResponseResult with mag
     */
    @PostMapping("/join")
    public ResponseResult<Object> joinOrganization(@RequestParam("organization_id") Integer organizationId) {
        Integer userId = userService.getId();
        Organization organization = organizationService.getById(organizationId);
        if (organization != null) {
            belongToService.insertNewBelongTo(organizationId, userId);
        } else {
            return ResponseResult.fail("Invalid organization Id");
        }
        return ResponseResult.suc("success");
    }


}

