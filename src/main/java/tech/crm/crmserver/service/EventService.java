package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dto.EventAttendDTO;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;
import tech.crm.crmserver.dto.TaskStatDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface EventService extends IService<Event> {

    /**
     * Query all the events of a user
     *
     * @param userId the id of the user to match
     * @return a list of events belongs to the user
     */
    public List<Event> queryEventByUserId(Integer userId);

    /**
     * Create a new event relate to the user
     *
     * @param userId the id of the user who create this event
     * @param eventsDTO the events information
     * @return if the creation is success or not
     */
    public boolean createNewEvent(Integer userId, EventsDTO eventsDTO);

    /**
     * update the information of a event
     * @param userId the user of this event
     * @param eventsUpdateDTO the update information
     */
    public void updateEvent(Integer userId, EventsUpdateDTO eventsUpdateDTO);

    /**
     * Delete the event by eventId
     * @param eventId the id of the event to delete
     */
    public void deleteEvent(Integer eventId, Integer userId);

    /**
     * Query event by some condition
     * @param eventId the id of the event to query
     * @param userId the user id of the event to query
     * @param startTime the start time of the event to query
     * @param finishTime the finish time of the event to query
     * @param status the status of the event to query
     * @return a list of match event
     */
    public List<Event> queryEvent(Integer eventId, Integer userId, LocalDateTime startTime, LocalDateTime finishTime, ToDoListStatus status, String description);

    /**
     * add a contact to the event
     * @param userId the user of this event
     * @param contactId the id of contact
     * @param eventId the id of event
     */
    public void addContact(Integer userId, Integer contactId, Integer eventId);

    /**
     * delete a contact from event
     * @param userId    the user of this event
     * @param attendId the id of attend
     */
    public void deleteContact(Integer userId, Integer attendId);

    /**
     * delete a contact from event
     * @param eventId the id of event
     * @return the eventAttendDTO include all the information like the contact of this event
     */
    public EventAttendDTO getEventById(Integer eventId);

    /**
     * Get events data for a user among given time
     * @param userId the id of user
     * @param startTime start of given time
     * @param finishTime end of given time
     * @return the list of event
     */
    public List<Event> getEventBetweenTime(Integer userId, LocalDateTime startTime, LocalDateTime finishTime);

    /**
     * get start time of event of this user
     * @param userId the id of user
     * @param startTime start of given time
     * @param finishTime end of given time
     * @return the start time list of event of this user
     */
    public List<LocalDateTime> getStartTimeByUserId(Integer userId, LocalDateTime startTime, LocalDateTime finishTime);

    /**
     * return the statistic information
     * @param userId the id of user
     * @param startTime start of given time
     * @param finishTime end of given time
     * @return the statistic information
     */
    public TaskStatDTO getStat(@Param("userId") Integer userId, LocalDateTime startTime, LocalDateTime finishTime);

    /**
     * Get events amount for a user among given month
     * @param userId the id of user
     * @param yearMonth the year and month for the event data
     * @param timeZoneOffset timezone offset in minutes
     * @return the list of event
     */
    public Map<LocalDate, Integer> getEventAmountByMonthYear(Integer userId, YearMonth yearMonth, Integer timeZoneOffset);

    /**
     * Get the date which has events
     * @param userId the id of user
     * @param yearMonth the year and month for the event data
     * @param timeZoneOffset timezone offset in minutes
     * @return the list of date which has event
     */
    public List<Integer> getEventDateByMonthYear(Integer userId, YearMonth yearMonth, Integer timeZoneOffset);
}
