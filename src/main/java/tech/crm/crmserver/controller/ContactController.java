package tech.crm.crmserver.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.common.constants.SecurityConstants;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.*;
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
                                                                            @RequestParam("department_id") Integer departmentId) {
        Integer userId = userService.getId();
        if (organizationId == null) {
            return ResponseResult.fail("missing organizationId");
        }

        List<Contact> contacts = contactService.getContactByOrgIdAndDepartmentId(organizationId, departmentId, userId);
        if (contacts.size() == 0) {
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
        String orgName = "OrgName";
        List<Organization> organizationAddTo = organizationService.getOrgBasedOnExactName(orgName);
        if (organizationAddTo.size() == 0) {
            return ResponseResult.fail("Organization does not exist");
        }

        // mock up department name
        String departName = "DepartName";
        Integer departmentAddTo = departmentService.getDepartIdByExactName(departName);
        if (departmentAddTo == null) {
            return ResponseResult.fail("Department does not exist");
        }

        // check the user's permission level
        Integer userID = userService.getId();
        Permission myPermission = permissionService.findPermission(departmentAddTo, userID);
        if (myPermission.getAuthorityLevel().getLevel() < 2) {
            return ResponseResult.fail("Do not have authority to add new contact");
        }
//        User user = userService.fromUserDTO(userDTO);
//        //check whether there is same email already exist
//        if(userService.register(user) == null){
//            return ResponseResult.fail("Same email already exist!");
//        }
//        //return token
//        String token = tokenKeyService.createToken(user);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.set(SecurityConstants.TOKEN_HEADER, token);
//        return ResponseResult.suc("successfully sign up!","",httpHeaders);
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

