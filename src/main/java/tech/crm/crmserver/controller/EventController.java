package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.exception.EventsFailAddedException;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dto.*;
import tech.crm.crmserver.service.AttendService;
import tech.crm.crmserver.service.EventService;
import tech.crm.crmserver.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.YearMonth;
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
        EventAttendDTO event = eventService.getEventById(eventId);
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
     * Get events data for a user among given time
     *
     * @return ResponseResult contain all the data about user's events
     */
    @GetMapping("/between")
    public ResponseResult<Object> getEventByUserIdAndTime(@RequestParam("start_time")LocalDateTime startTime,
                                                          @RequestParam("finish_time")LocalDateTime finishTime) {
        List<Event> events = eventService.getEventBetweenTime(userService.getId(),startTime,finishTime);
        return ResponseResult.suc("Query user's events success", events);
    }

    /**
     * Get events amount for a user among given year and month
     *
     * @return ResponseResult contain all the data about user's events
     */
    @GetMapping("/amount")
    public ResponseResult<Object> getEventByUserIdYearAndMonth(@RequestParam("year") Integer year,
                                                               @RequestParam("month") Integer month,
                                                               @RequestParam("time_zone") Integer timeZoneOffSet) {
        YearMonth yearMonth = YearMonth.of(year, month);
        List<Integer> dateHasEvent = eventService.getEventDateByMonthYear(userService.getId(), yearMonth, timeZoneOffSet);
        return ResponseResult.suc("Query date has events success", dateHasEvent);
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
     * @param attendId attend id
     * @return ResponseResult contain all information about if add is success or fail
     */
    @DeleteMapping("/contact")
    public ResponseResult<Object> deleteContact(@RequestParam("attend_id") Integer attendId){
        eventService.deleteContact(userService.getId(), attendId);
        return ResponseResult.suc("Delete contact success");
    }

    /**
     * get start time of all the events of this user
     * @return ResponseResult contain the start time list of event of this user
     */
    @GetMapping("/startTimes")
    public ResponseResult<Object> getStartTime(@RequestParam("start_time")LocalDateTime startTime,
                                               @RequestParam("finish_time")LocalDateTime finishTime){
        List<LocalDateTime> timeList = eventService.getStartTimeByUserId(userService.getId(),startTime,finishTime);
        return ResponseResult.suc("Successfully get the start times", timeList);
    }

    /**
     * get statistic information of events for this user
     * @return ResponseResult contain the start time list of events of this user
     */
    @GetMapping("/statistic")
    public ResponseResult<Object> getStat(@RequestParam("start_time")LocalDateTime startTime,
                                          @RequestParam("finish_time")LocalDateTime finishTime){
        TaskStatDTO stat = eventService.getStat(userService.getId(), startTime, finishTime);
        return ResponseResult.suc("Successfully get the statistic information", stat);
    }
}

