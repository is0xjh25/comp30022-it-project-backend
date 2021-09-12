package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.BelongTo;
import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.service.BelongToService;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.UserService;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-01
 */
@RestController
@RequestMapping("/belongTo")
public class BelongToController {

    @Autowired
    private BelongToService belongToService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @PostMapping()
    public ResponseResult createNewOrganization(@RequestParam("organization_id") Integer organizationId) {
        Integer userId = userService.getId();
        Organization organization = organizationService.getById(organizationId);
        boolean insertSucc = true;
        if (organization != null) {
            belongToService.insertNewBelongTo(organizationId, userId);
        } else {
            ResponseResult.fail("Invalid organization Id");
        }
        return ResponseResult.suc("success");
    }
}

