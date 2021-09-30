package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.exception.EventsFailAddedException;
import tech.crm.crmserver.service.EventService;
import tech.crm.crmserver.service.UserService;

import javax.validation.Valid;
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
@RequestMapping("/event")
public class EventController {

    @Autowired
    public UserService userService;

    @Autowired
    public EventService eventService;

    /**
     * Get all events data for a user
     *
     * @return ResponseResult contain all the data about user's events
     */
    @GetMapping
    public ResponseResult<Object> getAllEventByUserId() {
        List<Event> events = eventService.queryEventByUserId(userService.getId());
        return ResponseResult.suc("Query user's events success", events);
    }

    /**
     * Create new events
     *
     * @return ResponseResult contain all infomation about if creatation is success or fail
     */
    @PostMapping
    public ResponseResult<Object> createEvents(@RequestBody @Valid EventsDTO eventsDTO) {
        Integer userId = userService.getId();
        boolean creationSuccess = false;
        creationSuccess = eventService.createNewEvent(userId, eventsDTO);
        if (!creationSuccess) {
            throw new EventsFailAddedException();
        }
        return ResponseResult.suc("Adding event success");
    }
}

