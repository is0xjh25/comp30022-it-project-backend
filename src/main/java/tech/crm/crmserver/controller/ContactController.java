package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.*;
import tech.crm.crmserver.dto.ContactDTO;
import tech.crm.crmserver.service.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private PermissionService permissionService;

    /**
     * Search a contact's details
     * */
    @GetMapping("/details")
    public ResponseResult<Object> getContactById(@RequestParam("contact_id") Integer contactId) {
        Contact contact = contactService.getById(contactId);
        return ResponseResult.suc("success", contact);
    }

    @GetMapping()
    public ResponseResult<Object> getContactByOrganizationIdAndDepartmentId(@RequestParam("organization_id") Integer organizationId,
                                                                            @RequestParam("department_id") Integer departmentId,
                                                                            @RequestParam("size") Integer size,
                                                                            @RequestParam("current") Integer current) {
        Integer userId = userService.getId();
        if (organizationId == null) {
            return ResponseResult.fail("missing organizationId");
        }

        Page<Contact> contacts = contactService.getContactByOrgIdAndDepartmentId(new Page<>(current, size), organizationId, departmentId, userId);

        if (contacts.getTotal() == 0) {
            return ResponseResult.suc("No contacts");
        }
        return ResponseResult.suc("success", contacts);
    }

    @PostMapping()
    public ResponseResult<Object> createNewCustomer(@RequestBody ContactDTO contactDTO) {
        // read in the customer details
        Contact newContact = contactService.fromContactDTO(contactDTO);
        // check if the organization/department exists

        // let organization name
        // String orgName = "OrgName";
        // List<Organization> organizationAddTo = organizationService.getOrgBasedOnExactName(orgName);
        // if (organizationAddTo.size() == 0) {
        //     return ResponseResult.fail("Organization does not exist");
        // }
        // 1. check if departmentID is valid
        // 2. check if the user has permission to insert a new customer
        // 3. after all checkout, user ContactService to insert

        // mock up department name
        String departName = "DepartName";
        Integer departmentId = newContact.getDepartmentId();
        Department department = departmentService.getById(departmentId);
        Integer departmentAddTo = department.getId();
        if (department == null) {
            return ResponseResult.fail("Department does not exist");
        }

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentAddTo, userID);
        if (myPermission != null && myPermission.getAuthorityLevel().getLevel() < 2) {
            return ResponseResult.fail("Do not have authority to add new contact");
        }

        List<Contact> contacts = contactService.getContactBasedOnSomeConditionFromDB(null,newContact.getEmail(), null, null, null, null, null, null, null);
        if (contacts.size() > 0) {
            return ResponseResult.fail("Contact with same email already exist!");
        }
        boolean addSucc = false;
        try {
            addSucc = contactService.save(newContact);
        } catch (Exception e) {
            return ResponseResult.fail("Adding a new contact fail");
        }
        if (!addSucc) {
            return ResponseResult.fail("Adding a new contact fail");
        }
        return ResponseResult.suc("Add a new contact success");
    }

    @PutMapping
    public ResponseResult<Object> updateContact(@RequestBody ContactDTO contactDTO) {
        // For updating, it must have id
        Contact newContact = contactService.fromContactDTO(contactDTO);
        if (newContact.getId() == null) {
            return ResponseResult.fail("Missing id of the contact to be updated");
        }
        Integer departmentId = newContact.getDepartmentId();
        Department department = departmentService.getById(departmentId);
        Integer departmentAddTo = department.getId();
        if (department == null) {
            return ResponseResult.fail("Department does not exist");
        }

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentAddTo, userID);
        if (myPermission != null && myPermission.getAuthorityLevel().getLevel() < PermissionLevel.UPDATE.getLevel()) {
            return ResponseResult.fail("Do not have authority to update new contact");
        }

        if (contactService.updateContact(newContact)) {
            return ResponseResult.suc("update a new contact success");
        }
        return ResponseResult.fail("update a new contact fail");
    }

    @DeleteMapping
    public ResponseResult<Object> deleteContact(@RequestBody ContactDTO contactDTO) {

        Contact newContact = contactService.fromContactDTO(contactDTO);
        if (newContact.getId() == null) {
            return ResponseResult.fail("Missing id of the contact to be updated");
        }

        Integer departmentId = newContact.getDepartmentId();
        Department department = departmentService.getById(departmentId);
        Integer departmentAddTo = department.getId();
        if (department == null) {
            return ResponseResult.fail("Department does not exist");
        }

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentAddTo, userID);
        if (myPermission != null && myPermission.getAuthorityLevel().getLevel() < PermissionLevel.DELETE.getLevel()) {
            return ResponseResult.fail("Do not have authority to delete new contact");
        }

        if (contactService.removeById(newContact)) {
            return ResponseResult.suc("delete a new contact success");
        }
        return ResponseResult.fail("delete a new contact fail");
    }

    /**
     * search contacts by some details
     * @param
     * @return
     */
    @GetMapping("/search")
    public ResponseResult<Object> searchContact(@RequestParam(value = "firstName",required = false) String firstName,
                                                @RequestParam(value = "lastName",required = false) String lastName,
                                                @RequestParam(value = "gender",required = false) String gender,
                                                @RequestParam(value = "organization",required = false) String organization){
        Map<String,String> map = new HashMap<>();
        map.put("first_name",firstName);
        map.put("last_name",lastName);
        map.put("gender",gender);
        map.put("organization",organization);
        QueryWrapper<Contact> wrapper = new QueryWrapper<>();
        for(Map.Entry<String,String> entry : map.entrySet()){
            if(entry.getValue() == null){
                continue;
            }
            wrapper.like(entry.getKey(),entry.getValue());
        }
        List<Contact> contacts = contactService.list(wrapper);
        if(contacts.isEmpty()){
            return ResponseResult.fail("no match result is found");
        }
        return ResponseResult.suc("success",contacts);
    }
}

