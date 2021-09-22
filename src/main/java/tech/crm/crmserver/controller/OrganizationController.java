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
import tech.crm.crmserver.dao.*;
import tech.crm.crmserver.dto.DepartmentDTO;
import tech.crm.crmserver.dto.OrganizationDTO;
import tech.crm.crmserver.exception.DepartmentAlreadyExistException;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.exception.OrganizationNotExistException;
import tech.crm.crmserver.service.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
     * Create a department
     * the user need to be the owner of the department
     *
     * @param orgId which organization this department belong to
     * @param name the name of the department
     * @return the ResponseResult with msg
     */
    @PostMapping("/department")
    public ResponseResult<Object> createDepartment(@RequestParam("organization_id") Integer orgId,
                                           @RequestParam("department_name") String name){
        departmentService.createDepartment(orgId,name);
        return ResponseResult.suc("Successfully create department!");
    }


    /**
     * Find all the departments in the organization
     *
     * @param organizationId the id of organization user want to search
     * @return the ResponseResult with msg and department data
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
     *
     * @return the ResponseResult with msg, with all the match organizations
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
        organizationDTOS.sort(new Comparator<OrganizationDTO>() {
            @Override
            public int compare(OrganizationDTO o1, OrganizationDTO o2) {
                if (o1.isOwner() && !o2.isOwner()) {
                    return -1;
                } else if (!o1.isOwner() && o2.isOwner()){
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        return ResponseResult.suc("success", organizationDTOS);
    }

    /**
     * Get Organization based on organization id
     *
     * @param organizationId the id of organization user want to search
     * @return the ResponseResult with msg, with the match organization
     */
    @GetMapping()
    public ResponseResult<Object> getOrganization(@RequestParam("organization_id") Integer organizationId) {
        Organization organization = organizationService.getById(organizationId);
        if (organization != null) {
            return ResponseResult.suc("success", organization);
        }
        return ResponseResult.fail("No content");
    }

    /**
     * Get Organization based on organization Integer
     *
     * @param organizationName the exact name of organization user want to search
     * @return the ResponseResult with msg, with the match organizations
     */
    @GetMapping("/name")
    public ResponseResult<Object> getOrganizationBasedOnName(@RequestParam("organization_name") String organizationName) {
        List<Organization> organizations = organizationService.getOrgBasedOnName(organizationName);
        if (organizations.size() == 0) {
            return ResponseResult.fail("No match organization");
        }
        return ResponseResult.suc("success", organizations);
    }

    /**
     * Create new Organization
     *
     * @param organizationName the name of organization user want to create
     * @return the ResponseResult with msg
     */
    @PostMapping()
    public ResponseResult<Object> createNewOrganization(@RequestParam("organization_name") String organizationName) {
        // todo
        Integer userId = userService.getId();
        List<Organization> organizationListWithSameName = organizationService.getOrgBasedOnExactName(organizationName);

        if (organizationListWithSameName.size() > 0) {
            return ResponseResult.fail("Organization with same name exists");
        }
        Organization newOrganization = new Organization();
        newOrganization.setName(organizationName);
        newOrganization.setOwner(userId);
        try {
            organizationService.save(newOrganization);
        } catch (Exception e) {
            return ResponseResult.fail("Fail to create organization");
        }
        Organization organization = organizationService.getOrgBasedOnExactName(organizationName).get(0);
        belongToService.insertNewBelongTo(organization.getId(), userId);
        return ResponseResult.suc("success");
    }

    /**
     * Delete the organization, belongTo, departments, permission in it<br/>
     * will check the permission
     *
     * @param organizationId the organization id of organization need to be deleted
     * @return ResponseResult with mag
     */
    @DeleteMapping()
    public ResponseResult<Object> deleteOrganization(@RequestParam("organization_id") Integer organizationId){
        organizationService.deleteOrganization(organizationId);
        return ResponseResult.suc("success");
    }

    /**
     * Join a organization
     *
     * @param organizationId the id of organization user want to join
     * @return ResponseResult with mag
     */
    @PostMapping("/join")
    public ResponseResult<Object> joinOrganization(@RequestParam("organization_id") Integer organizationId) {
        Integer userId = userService.getId();
        Organization organization = organizationService.getById(organizationId);

        List<BelongTo> belongToList = belongToService.queryBelongToRelation(null, userId, organizationId, null);
        if (belongToList.size() > 0) {
            return ResponseResult.fail("You have been in the organization");
        }
        if (organization != null) {
            belongToService.insertNewBelongTo(organizationId, userId);
        } else {
            return ResponseResult.fail("Invalid organization Id");
        }
        return ResponseResult.suc("success");
    }
}

