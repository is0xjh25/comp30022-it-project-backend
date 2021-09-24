package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Department;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.ContactCreateDTO;
import tech.crm.crmserver.dto.ContactDTO;
import tech.crm.crmserver.dto.ContactUpdateDTO;
import tech.crm.crmserver.exception.*;
import tech.crm.crmserver.service.ContactService;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.PermissionService;
import tech.crm.crmserver.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
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
    private DepartmentService departmentService;

    @Autowired
    private PermissionService permissionService;

    /**
     * Search contact by contact id
     *
     * @param contactId the id of contact searching
     * @return ResponseResult about if the search success, or why it fail, with contact data
     */
    @GetMapping("/detail")
    public ResponseResult<Object> getContactById(@RequestParam("contact_id") Integer contactId){
        Contact contact = contactService.getById(contactId);
        if (contact != null) {
            ContactDTO contactDTO = contactService.ContactToContactDTO(contact);
            return ResponseResult.suc("success", contactDTO);
        }
        throw new ContactNotExistException();
    }

    /**
     * Search contact based on organizationId, departmentId
     *
     * @param organizationId the id of organization
     * @param departmentId the id of department
     * @param size the size of the page
     * @param current the value of current page
     * @return ResponseResult about if the search success, or why it fail, with contact data
     */
    @GetMapping()
    public ResponseResult<Object> getContactByOrganizationIdAndDepartmentId(@RequestParam("organization_id") Integer organizationId,
                                                                            @RequestParam(value = "department_id", required = false) Integer departmentId,
                                                                            @RequestParam("size") Integer size,
                                                                            @RequestParam("current") Integer current) {
        Integer userId = userService.getId();
        if (organizationId == null) {
            throw new OrganizationNotExistException();
        }

        Page<Contact> contacts = contactService.getContactByOrgIdAndDepartmentId(new Page<>(current, size), organizationId, departmentId, userId);
        //transfer into ContactDTO
        List<Contact> records = contacts.getRecords();
        List<ContactDTO> recordDTOs = new ArrayList<>();
        for(Contact contact: records){
            recordDTOs.add(contactService.ContactToContactDTO(contact));
        }
        Page<ContactDTO> contactDTOs = new Page<>(contacts.getCurrent(),contacts.getSize(),contacts.getTotal());
        contactDTOs.setRecords(recordDTOs);
        return ResponseResult.suc("success", contactDTOs);
    }

    /**
     * Add a contact into the department
     *
     * @param contactCreateDTO the details of contact to add
     * @return ResponseResult about if the adding success, or why it fail
     */
    @PostMapping()
    public ResponseResult<Object> createNewCustomer(@RequestBody @Valid ContactCreateDTO contactCreateDTO) {
        // read in the customer details
        Contact newContact = contactService.fromContactCreateDTO(contactCreateDTO);

        Integer departmentId = newContact.getDepartmentId();
        Department department = departmentService.getById(departmentId);
        if (department == null) {
            throw new DepartmentNotExistException();
        }
        Integer departmentAddTo = department.getId();

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentAddTo, userID);
        if (myPermission == null || myPermission.getAuthorityLevel().getLevel() < PermissionLevel.UPDATE.getLevel()) {
            throw new NotEnoughPermissionException();
        }

        // check if the same contact already exists
        List<Contact> contacts = contactService.getContactBasedOnSomeConditionFromDB(departmentAddTo,newContact.getEmail(), null, null, null, null, null, null, null);
        if (contacts.size() > 0) {
            throw new ContactAlreadyExistException();
        }
        boolean addSucc = false;
        try {
            addSucc = contactService.save(newContact);
        } catch (Exception e) {
            throw new ContactFailAddedException();
        }
        if (!addSucc) {
            throw new ContactFailAddedException();
        }
        return ResponseResult.suc("Adding a new contact success");
    }

    /**
     * Update a contact's into
     *
     * @param contactDTO the details of contact to update
     * @return ResponseResult about if the update success, or why it fail
     */
    @PutMapping
    public ResponseResult<Object> updateContact(@RequestBody @Valid ContactUpdateDTO contactDTO) {
        // For updating, it must have id
        Contact newContact = contactService.fromContactUpdateDTO(contactDTO);
        Contact oldContact = contactService.getById(newContact.getId());
        if(oldContact == null){
            throw new ContactNotExistException();
        }
        Integer departmentId = oldContact.getDepartmentId();
        Department department = departmentService.getById(departmentId);
        if (department == null) {
            throw new DepartmentNotExistException();
        }
        Integer departmentAddTo = department.getId();

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentAddTo, userID);
        if (myPermission == null || myPermission.getAuthorityLevel().getLevel() < PermissionLevel.UPDATE.getLevel()) {
            throw new NotEnoughPermissionException();
        }

        if (contactService.updateContact(newContact)) {
            return ResponseResult.suc("update a new contact success");
        }
        throw new ContactFailUpdateException();
    }

    /**
     * Delete a contact's into
     *
     * @param contactId the details of contact to delete
     * @return ResponseResult about if the delete success, or why it fail
     */
    @DeleteMapping
    public ResponseResult<Object> deleteContact(@RequestParam("contact_id") Integer contactId) {

        Contact contact = contactService.getById(contactId);
        Integer departmentId = contact.getDepartmentId();

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentId, userID);
        if (myPermission == null || myPermission.getAuthorityLevel().getLevel() < PermissionLevel.DELETE.getLevel()) {
            throw new NotEnoughPermissionException();
        }

        if (contactService.removeById(contactId)) {
            return ResponseResult.suc("delete a new contact success");
        }
        throw new ContactFailDeleteException();
    }

    /**
     * Search contacts by some details
     *
     * @return ResponseResult about if the search success, or why it fail
     */
    @GetMapping("/search")
    public ResponseResult<Object> searchContact(@RequestParam("department_id") Integer departmentId,
                                                @RequestParam("search_key") String searchKey){
        Permission myPermission = permissionService.findPermission(departmentId, userService.getId());
        if (myPermission == null || myPermission.getAuthorityLevel().getLevel() < PermissionLevel.DISPLAY.getLevel()) {
            throw new NotEnoughPermissionException();
        }
        Map<String,String> map = new HashMap<>();
        map.put("email",searchKey);
        map.put("first_name",searchKey);
        map.put("last_name",searchKey);
        map.put("gender",searchKey);
        map.put("organization",searchKey);
        map.put("description",searchKey);
        QueryWrapper<Contact> wrapper;
        List<ContactDTO> contacts = new ArrayList<>();
        for(Map.Entry<String,String> entry : map.entrySet()){
            wrapper = new QueryWrapper<>();
            wrapper.eq("department_id",departmentId);
            wrapper.like(entry.getKey(),entry.getValue());
            contacts.addAll(contactService.ContactToContactDTO(contactService.list(wrapper)));
        }

        if(contacts.isEmpty()){
            throw new ContactNotFoundException();
        }
        return ResponseResult.suc("success",contacts);
    }
}

