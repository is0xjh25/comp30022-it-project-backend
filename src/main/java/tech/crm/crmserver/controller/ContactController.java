package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Contact;
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

        Page<ContactDTO> contactDTOs = contactService.getContactByOrgIdAndDepartmentId(new Page<>(current, size), organizationId, departmentId, userId);
        //transfer into ContactDTO
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
        boolean addSucc = false;
        // All the verify would be down in here
        addSucc = contactService.createContactByContactDTO(contactCreateDTO, userService.getId());
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
        boolean updateSucc = false;
        updateSucc = contactService.updateContactByContactDTO(contactDTO, userService.getId());
        if (!updateSucc) {
            throw new ContactFailUpdateException();
        }
        return ResponseResult.suc("Updating contact success");
    }

    /**
     * Delete a contact's into
     *
     * @param contactId the details of contact to delete
     * @return ResponseResult about if the delete success, or why it fail
     */
    @DeleteMapping
    public ResponseResult<Object> deleteContact(@RequestParam("contact_id") Integer contactId) {

        boolean deleteSucc = false;
        deleteSucc = contactService.deleteContactByContactId(contactId, userService.getId());
        if (!deleteSucc) {
            throw new ContactFailDeleteException();
        }
        return ResponseResult.suc("Deleting contact success");
    }

    /**
     * Search contacts by some details
     *
     * @return ResponseResult about if the search success, or why it fail
     */
    @GetMapping("/search")
    public ResponseResult<Object> searchContact(@RequestParam("department_id") Integer departmentId,
                                                @RequestParam("search_key") String searchKey,
                                                @RequestParam("size") Integer size,
                                                @RequestParam("current") Integer current){
        Permission myPermission = permissionService.findPermission(departmentId, userService.getId());
        if (myPermission == null || myPermission.getAuthorityLevel().getLevel() < PermissionLevel.DISPLAY.getLevel()) {
            throw new NotEnoughPermissionException();
        }
        Page<ContactDTO> contactDTOPage = contactService.searchContactDTO(new Page<>(current, size), departmentId, searchKey);
        return ResponseResult.suc("success",contactDTOPage);
    }
}

