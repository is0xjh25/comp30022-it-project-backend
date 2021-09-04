package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.service.ContactService;

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

    /**
     * Search a contact's details
     * */
    @GetMapping("/detail")
    public ResponseResult<Object> getContactById(@RequestParam("contact_id") Integer contactId) {
        Contact contact = contactService.getById(contactId);
        return ResponseResult.suc("success", contact);
    }
}

