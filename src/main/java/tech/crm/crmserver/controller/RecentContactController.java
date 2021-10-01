package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dto.RecentContactDTO;
import tech.crm.crmserver.service.RecentContactService;
import tech.crm.crmserver.service.UserService;

import java.util.List;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-20
 */
@RestController
@RequestMapping("/recentContact")
public class RecentContactController {

    @Autowired
    private RecentContactService recentContactService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseResult<Object> getRecentContact(@RequestParam(value = "limit", required = false) Integer limit){
        List<RecentContactDTO> recentContactDTOList = recentContactService.getContactByUserId(userService.getId(), limit);
        return ResponseResult.suc("success", recentContactDTOList);
    }
}

