package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import tech.crm.crmserver.dao.Organization;
import tech.crm.crmserver.dto.OrganizationDTO;
import tech.crm.crmserver.service.OrganizationService;
import tech.crm.crmserver.service.UserService;

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
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

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
    public Organization getOrganizationBasedOnName(@RequestParam String organizationName) {
        return null;
    }
}

