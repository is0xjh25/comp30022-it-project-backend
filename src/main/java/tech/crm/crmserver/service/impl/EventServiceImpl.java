package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.constants.EmailConstants;
import tech.crm.crmserver.common.enums.PermissionLevel;
import tech.crm.crmserver.dao.*;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.common.utils.NullAwareBeanUtilsBean;
import tech.crm.crmserver.dao.Attend;
import tech.crm.crmserver.dao.Contact;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.ContactAttendDTO;
import tech.crm.crmserver.dto.EventAttendDTO;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;
import tech.crm.crmserver.exception.FailToAddContactToEventException;
import tech.crm.crmserver.exception.FailToDeleteContactToEventException;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.mapper.ContactMapper;
import tech.crm.crmserver.exception.*;
import tech.crm.crmserver.mapper.EventMapper;
import tech.crm.crmserver.service.AttendService;
import tech.crm.crmserver.service.ContactService;
import tech.crm.crmserver.service.EventService;
import tech.crm.crmserver.service.*;

import java.time.LocalDateTime;
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
     * @param finishTIme the finish time of the event to query
     * @param status the status of the event to query
     * @param description the description of the event
     * @return a list of match event
     */
    @Override
    public List<Event> queryEvent(Integer eventId, Integer userId, LocalDateTime startTime, LocalDateTime finishTIme, ToDoListStatus status, String description) {
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
        if (finishTIme != null) {
            eventQueryWrapper.eq("finish_time", finishTIme);
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
}
