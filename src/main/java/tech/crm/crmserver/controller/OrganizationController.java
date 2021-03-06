package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.enums.Status;
import tech.crm.crmserver.common.exception.*;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.BelongTo;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.DepartmentDTO;
import tech.crm.crmserver.dto.OrganizationDTO;
import tech.crm.crmserver.dto.PageDTO;
import tech.crm.crmserver.dto.UserPermissionDTO;
import tech.crm.crmserver.service.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
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
        throw new OrganizationNotExistException();
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
            throw new OrganizationNotFoundException();
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
            throw new OrganizationAlreadyExistException();
        }
        Organization newOrganization = new Organization();
        newOrganization.setName(organizationName);
        newOrganization.setOwner(userId);
        try {
            organizationService.save(newOrganization);
        } catch (Exception e) {
            throw new FailToCreateOrganizationException();
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
     * @return ResponseResult with msg
     */
    @PostMapping("/join")
    public ResponseResult<Object> joinOrganization(@RequestParam("organization_id") Integer organizationId) {
        Integer userId = userService.getId();
        Organization organization = organizationService.getById(organizationId);

        List<BelongTo> belongToList = belongToService.queryBelongToRelation(null, userId, organizationId, null);
        if (belongToList.size() > 0) {
            throw new UserAlreadyInOrganizationException();
        }
        if (organization != null) {
            belongToService.insertNewBelongTo(organizationId, userId);
        } else {
            throw new OrganizationNotExistException();
        }
        return ResponseResult.suc("success");
    }

    /**
     * transfer the owner of the organization to another user in organization<br/>
     * only the owner of the organization has the permission to do that
     * @param organizationId id of the organization
     * @param to new owner of organization
     * @return ResponseResult with msg
     */
    @PutMapping("/transfer")
    public ResponseResult<Object> transferOwnershipOfOrganization(@RequestParam("organization_id") Integer organizationId,
                                                                  @RequestParam("new_owner") Integer to){
        organizationService.transferOwnershipOfOrganization(organizationId,userService.getId(),to);
        return ResponseResult.suc("success");
    }

    /**
     * search member in organization<br/>
     * only the owner has the permission to do this action
     * @param organizationId the id of organization
     * @param searchKey search key
     * @return ResponseResult with msg
     */
    @GetMapping("/searchMember")
    public ResponseResult<Object> searchMemberInOrganization(@RequestParam("organization_id") Integer organizationId,
                                                             @RequestParam("search_key") String searchKey,
                                                             @Valid PageDTO pageDTO){
        Organization organization = organizationService.getById(organizationId);
        //check permission
        if (organization == null){
            throw new OrganizationNotExistException();
        }
        if(!organization.getOwner().equals(userService.getId())) {
            throw new NotEnoughPermissionException();
        }
        Page<UserPermissionDTO> userPermissionDTOPage = organizationService.searchMember(new Page<>(pageDTO.getCurrent(), pageDTO.getSize()), organizationId, searchKey);
        return ResponseResult.suc("success",userPermissionDTOPage);
    }

    @DeleteMapping("/leave")
    public ResponseResult<Object> leaveOrganization(@RequestParam("organization_id") Integer organizationId){
        Integer userId = userService.getId();
        Organization organization = organizationService.getById(organizationId);
        if(organization == null){
            throw new OrganizationNotExistException();
        }
        if(organization.getOwner().equals(userId)){
            throw new UserAlreadyOwnOrganizationException();
        }
        organizationService.leaveOrganization(userId,organizationId);
        return ResponseResult.suc("success");
    }
}

