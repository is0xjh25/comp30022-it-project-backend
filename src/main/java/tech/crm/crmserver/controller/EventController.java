package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dto.EventContactDTO;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;
import tech.crm.crmserver.exception.EventsFailAddedException;
import tech.crm.crmserver.service.AttendService;
import tech.crm.crmserver.exception.EventsFailQueryException;
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

    @GetMapping
    public ResponseResult<Object> getEventById(@RequestParam("event_id") Integer eventId) {
        Event event = eventService.getById(eventId);
        if (event == null) {
            throw new EventsFailQueryException();
        }
        return ResponseResult.suc("Get event details success", event);
    }

    /**
     * Get all events data for a user
     *
     * @return ResponseResult contain all the data about user's events
     */
    @GetMapping("/myEvents")
    public ResponseResult<Object> getAllEventByUserId() {
        List<Event> events = eventService.queryEventByUserId(userService.getId());
        return ResponseResult.suc("Query user's events success", events);
    }

    /**
     * Create new events
     *
     * @param eventsDTO the dto entity of event, it contains start time, end time, description
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
     * Delete a events
     *
     * @param eventId the event id to delete
     * @return ResponseResult contain if delete success or not
     */
    @DeleteMapping
    public ResponseResult<Object> deleteEventsById(@RequestParam("event_id") Integer eventId) {
        eventService.deleteEvent(eventId, userService.getId());
        return ResponseResult.suc("Delete event success");
    }

    /**
     * add a contact to event
     * @param eventContactDTO contact id and event id
     * @return ResponseResult contain all information about if add is success or fail
     */
    @PostMapping("/contact")
    public ResponseResult<Object> addContact(@RequestBody EventContactDTO eventContactDTO){
        eventService.addContact(userService.getId(),eventContactDTO.getContactId(),eventContactDTO.getEventId());
        return ResponseResult.suc("Add contact success");
    }

    /**
     * delete a contact from event
     * @param eventContactDTO contact id and event id
     * @return ResponseResult contain all information about if add is success or fail
     */
    @DeleteMapping("/contact")
    public ResponseResult<Object> deleteContact(@RequestBody EventContactDTO eventContactDTO){
        eventService.deleteContact(userService.getId(),eventContactDTO.getContactId(),eventContactDTO.getEventId());
        return ResponseResult.suc("Delete contact success");
    }
}

