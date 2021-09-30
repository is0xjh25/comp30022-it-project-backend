package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;
import tech.crm.crmserver.exception.EventsFailAddedException;
import tech.crm.crmserver.service.AttendService;
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

    @Autowired
    public AttendService attendService;

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
     * @return ResponseResult contain all information about if creation is success or fail
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

    /**
     * Update the events by id
     *
     * @param eventsUpdateDTO the update information
     * @return ResponseResult contain all information about if update is success or fail
     */
    @PutMapping
    public ResponseResult<Object> updateEvents(@RequestBody @Valid EventsUpdateDTO eventsUpdateDTO){
        eventService.updateEvent(userService.getId(),eventsUpdateDTO);
        return ResponseResult.suc("Update event success");
    }

    /**
     * add a contact to event
     * @param contactId the id of contact
     * @param eventId the id of event
     * @return ResponseResult contain all information about if add is success or fail
     */
    @PostMapping("/addContact")
    public ResponseResult<Object> addContact(@RequestParam("contact_id") Integer contactId,
                                             @RequestParam("event_id") Integer eventId){
        eventService.addContact(userService.getId(),contactId,eventId);
        return ResponseResult.suc("Add contact success");
    }

}

