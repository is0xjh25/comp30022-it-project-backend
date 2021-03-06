package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.common.exception.EventsNotExistException;
import tech.crm.crmserver.common.exception.FailToAddContactToEventException;
import tech.crm.crmserver.common.exception.FailToDeleteContactToEventException;
import tech.crm.crmserver.common.exception.NotEnoughPermissionException;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Attend;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.Permission;
import tech.crm.crmserver.dto.*;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.mapper.EventMapper;
import tech.crm.crmserver.service.AttendService;
import tech.crm.crmserver.service.DepartmentService;
import tech.crm.crmserver.service.EventService;
import tech.crm.crmserver.service.PermissionService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private AttendService attendService;

    @Autowired
    public DepartmentService departmentService;

    @Autowired
    public PermissionService permissionService;

    @Autowired
    private ContactMapper contactMapper;


    /**
     * check whether the user is the owner of event
     * @param userId the id of user
     * @param eventId the id of event
     */
    private void checkUser(Integer userId, Integer eventId) {
        //check user
        Event event = baseMapper.selectById(eventId);
        if(event == null){
            throw new EventsNotExistException();
        }
        if(!event.getUserId().equals(userId)){
            throw new NotEnoughPermissionException();
        }
    }

    /**
     * Query all the events of a user
     *
     * @param userId the id of the user to match
     * @return a list of events belongs to the user
     */
    @Override
    public List<Event> queryEventByUserId(Integer userId) {
        return eventMapper.getEventsByUserId(userId);
    }

    /**
     * Create a new event relate to the user
     *
     * @param userId the id of the user who create this event
     * @param eventsDTO the events information
     * @return if the creation is success or not
     */
    @Override
    public boolean createNewEvent(Integer userId, EventsDTO eventsDTO) {
        Event event = new Event();
        event.setUserId(userId);
        event.setStartTime(eventsDTO.getStartTime());
        event.setFinishTime(eventsDTO.getFinishTime());
        event.setDescription(eventsDTO.getDescription());
        return save(event);
    }

    /**
     * update the information of a event
     *
     * @param userId          the user of this event
     * @param eventsUpdateDTO the update information
     */
    @Override
    public void updateEvent(Integer userId, EventsUpdateDTO eventsUpdateDTO) {
        //check user
        checkUser(userId,eventsUpdateDTO.getId());

        Event event = new Event();
        NullAwareBeanUtilsBean.copyProperties(eventsUpdateDTO,event);
        System.out.println(event);

        //update to database
        updateById(event);
    }

    /**
     * Delete the event by eventId
     *
     * @param eventId the id of the event to delete
     */
    @Override
    public void deleteEvent(Integer eventId, Integer userId) {
        //check user
        checkUser(userId,eventId);

        attendService.deleteAttendByEventId(eventId);
        removeById(eventId);
    }

    /**
     * Query event by some condition
     * @param eventId the id of the event to query
     * @param userId the user id of the event to query
     * @param startTime the start time of the event to query
     * @param finishTime the finish time of the event to query
     * @param status the status of the event to query
     * @param description the description of the event
     * @return a list of match event
     */
    @Override
    public List<Event> queryEvent(Integer eventId, Integer userId, LocalDateTime startTime, LocalDateTime finishTime, ToDoListStatus status, String description) {
        QueryWrapper<Event> eventQueryWrapper = new QueryWrapper<>();
        if (eventId != null) {
            eventQueryWrapper.eq("id", eventId);
        }
        if (userId != null) {
            eventQueryWrapper.eq("user_id", userId);
        }
        if (startTime != null) {
            eventQueryWrapper.eq("start_time", startTime);
        }
        if (finishTime != null) {
            eventQueryWrapper.eq("finish_time", finishTime);
        }
        if (status != null) {
            eventQueryWrapper.eq("status", status.getStatus());
        }
        if (description != null) {
            eventQueryWrapper.eq("description", description);
        }
        return eventMapper.selectList(eventQueryWrapper);
    }

    /**
     * add a contact to the event
     *
     * @param userId    the user of this event
     * @param contactId the id of contact
     * @param eventId   the id of event
     */
    @Override
    public void addContact(Integer userId, Integer contactId, Integer eventId) {
        //check user
        checkUser(userId,eventId);

        //check whether user has the permission to view the contact
        Permission permission = permissionService.getPermissionByUserIdAndContactId(userId, contactId);
        if(permission == null || permission.getAuthorityLevel().equals(PermissionLevel.PENDING)){
            throw new NotEnoughPermissionException();
        }

        Attend attend = new Attend();
        attend.setContactId(contactId);
        attend.setEventId(eventId);

        if (!attendService.save(attend)) {
            throw new FailToAddContactToEventException();
        }
    }

    /**
     * delete a contact from event
     *
     * @param userId   the user of this event
     * @param attendId the id of attend
     */
    @Override
    public void deleteContact(Integer userId, Integer attendId) {
        Attend attend = attendService.getById(attendId);

        //check user
        checkUser(userId,attend.getEventId());

        if(!attendService.removeById(attendId)){
            throw new FailToDeleteContactToEventException();
        }
    }

    /**
     * get event details
     * @param eventId the id of event
     * @return the eventAttendDTO include all the information like the contact of this event
     */
    @Override
    public EventAttendDTO getEventById(Integer eventId) {
        Event event = getById(eventId);
        if (event == null) {
            throw new EventsNotExistException();
        }
        List<Attend> attendList = attendService.getAttendByEventId(eventId);
        Map<Integer, Integer> contactAttendMap = new HashMap<>();

        for (Attend attend : attendList) {
            contactAttendMap.put(attend.getContactId(), attend.getId());
        }

        List<Integer> contactIdList = new ArrayList<>();
        for (Attend attend : attendList) {
            contactIdList.add(attend.getContactId());
        }
        List<Contact> contactList = new ArrayList<>();
        if (contactIdList.size() != 0) {
            contactList = contactMapper.selectBatchIds(contactIdList);
        }
        List<ContactAttendDTO> contactAttendDTOList = new ArrayList<>();
        for (Contact contact : contactList) {
            ContactAttendDTO contactAttendDTO = new ContactAttendDTO();
            NullAwareBeanUtilsBean.copyProperties(contact, contactAttendDTO);
            contactAttendDTO.setAttendId(contactAttendMap.get(contact.getId()));
            contactAttendDTOList.add(contactAttendDTO);
        }
        EventAttendDTO eventAttendDTO = new EventAttendDTO();
        eventAttendDTO.setId(event.getId());
        eventAttendDTO.setUserId(event.getUserId());
        eventAttendDTO.setDescription(event.getDescription());
        eventAttendDTO.setStartTime(event.getStartTime());
        eventAttendDTO.setFinishTime(event.getFinishTime());
        eventAttendDTO.setStatus(event.getStatus());
        eventAttendDTO.setContactList(contactAttendDTOList);
        return eventAttendDTO;
    }

    /**
     * Get events data for a user among given time
     * @param userId the id of user
     * @param startTime start of given time
     * @param finishTime end of given time
     * @return the list of event
     */
    @Override
    public List<Event> getEventBetweenTime(Integer userId, LocalDateTime startTime, LocalDateTime finishTime) {
        return baseMapper.getEventsBetween(userId,startTime,finishTime);
    }

    /**
     * get start time of event of this user
     *
     * @param userId the id of user
     * @param startTime start of given time
     * @param finishTime end of given time
     * @return the start time list of event of this user
     */
    @Override
    public List<LocalDateTime> getStartTimeByUserId(Integer userId, LocalDateTime startTime, LocalDateTime finishTime) {
        return baseMapper.getStartTime(userId,startTime,finishTime);
    }

    /**
     * return the statistic information
     *
     * @param userId     the id of user
     * @param startTime  start of given time
     * @param finishTime end of given time
     * @return the statistic information
     */
    @Override
    public TaskStatDTO getStat(Integer userId, LocalDateTime startTime, LocalDateTime finishTime) {
        return baseMapper.getStat(userId,startTime,finishTime);
    }

    /**
     * Get events amount for a user among given month
     * @param userId the id of user
     * @param yearMonth the year and month for the event data
     * @param timeZoneOffset timezone offset in minutes
     * @return the list of event
     */
    public Map<LocalDate, Integer> getEventAmountByMonthYear(Integer userId, YearMonth yearMonth, Integer timeZoneOffset) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.plusMonths(1).atDay(1);
        List<Event> eventList = baseMapper.getEventsBetween(userId,startDate.atStartOfDay(),endDate.atStartOfDay());
        Map<LocalDate, Integer> map = new HashMap<>();
        Integer totalNumberofDate = yearMonth.lengthOfMonth();
        for (int i = 1; i< totalNumberofDate; i++) {
            LocalDate currentDate = yearMonth.atDay(i);
            LocalDateTime startOfDay = LocalDateTime.of(currentDate, LocalTime.MIDNIGHT);
            LocalDateTime endOfDay = LocalDateTime.of(currentDate, LocalTime.MAX);
            startOfDay = startOfDay.plusMinutes(timeZoneOffset);
            endOfDay = endOfDay.plusMinutes(timeZoneOffset);
            int amount = 0;
            for (Event event: eventList) {
                LocalDateTime eventStartTime = event.getStartTime();
                LocalDateTime eventEndTime = event.getFinishTime();
                if ((eventStartTime.isAfter(startOfDay) && eventStartTime.isBefore(endOfDay)) || (eventEndTime.isAfter(startOfDay) && eventEndTime.isBefore(endOfDay)) || (eventStartTime.isBefore(startOfDay) && eventEndTime.isAfter(endOfDay))) {
                    amount++;
                }
            }
            if (amount > 0) {
                map.put(currentDate, amount);
            }
        }
        return map;
    }

    /**
     * Get the date which has events
     * @param userId the id of user
     * @param yearMonth the year and month for the event data
     * @param timeZoneOffset timezone offset in minutes
     * @return the list of date which has event
     */
    public List<Integer> getEventDateByMonthYear(Integer userId, YearMonth yearMonth, Integer timeZoneOffset) {
        Map<LocalDate, Integer> eventsMap = getEventAmountByMonthYear(userId, yearMonth, timeZoneOffset);
        List<Integer> dateHasEvents = new ArrayList<>();
        for (LocalDate localDate : eventsMap.keySet()) {
            dateHasEvents.add(localDate.getDayOfMonth());
        }
        return dateHasEvents;
    }
}
