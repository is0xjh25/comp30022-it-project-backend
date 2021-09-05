package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> createDepartment(@RequestParam("organization_id") Integer org_id,
                                 @RequestParam("department_name") String name){
        Organization organization = null;
        try{
            organization = organizationService.getById(org_id);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //organization not exist
        if(organization == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        //check the authority(creator should be the owner of the organization)
        if(organization.getOwner() != userService.getId()){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Department department = new Department();
        department.setOrganizationId(org_id);
        department.setName(name);
        try {
            departmentService.save(department);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Check out all organization this user belongs to
    @GetMapping("/myOrganization")
    public List<Organization> getAllOrganization() {
        Integer userId = userService.getId();
        return organizationService.getAllOrgUserOwnAndBelongTo(userId);
    }

    /**
     * Get Organization based on organization Integer
     * */
    @GetMapping()
    public Organization getOrganization(@RequestParam("organization_id") Integer organizationId) {
        return organizationService.getById(organizationId);
    }

    /**
     * Get Organization based on organization Integer
     * */
    @GetMapping("/name")
    public List<Organization> getOrganizationBasedOnName(@RequestParam String organizationName) {
        List<Organization> organizations = organizationService.getOrgBasedOnName(organizationName);
        return organizations;
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

