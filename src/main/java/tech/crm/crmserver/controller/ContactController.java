package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.service.ContactService;

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

    /**
     * Search a contact's details
     * */
    @GetMapping("/detail")
    public ResponseResult<Object> getContactById(@RequestParam("contact_id") Integer contactId) {
        Contact contact = contactService.getById(contactId);
        return ResponseResult.suc("success", contact);
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

