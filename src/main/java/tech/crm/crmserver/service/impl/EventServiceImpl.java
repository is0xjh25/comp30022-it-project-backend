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
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;
import tech.crm.crmserver.exception.*;
import tech.crm.crmserver.mapper.EventMapper;
import tech.crm.crmserver.service.*;

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
    private ContactService contactService;

    @Autowired
    public DepartmentService departmentService;

    @Autowired
    public PermissionService permissionService;


    /**
     * check whether the user is the owner of event
     * @param userId the id of user
     * @param eventId the id of event
     */
    private void checkUser(Integer userId, Integer eventId) {
        //check user
        Event event = baseMapper.selectById(eventId);
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

        UpdateWrapper<Event> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",eventsUpdateDTO.getId());
        Map<String, String> map = new ObjectMapper().convertValue(eventsUpdateDTO, Map.class);
        //translate to underscore
        PropertyNamingStrategy.SnakeCaseStrategy snakeCaseStrategy = new PropertyNamingStrategy.SnakeCaseStrategy();
        for(Map.Entry<String,String> entry : map.entrySet()){
            if(entry.getValue() != null && !entry.getKey().equals("id")){
                updateWrapper.set(snakeCaseStrategy.translate(entry.getKey()),entry.getValue());
            }
        }

        //update to database
        update(updateWrapper);
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
        Contact contact = contactService.getById(contactId);
        if(contact == null){
            throw new ContactNotExistException();
        }
        Department department = departmentService.getById(contact.getDepartmentId());
        if(department == null){
            throw new DepartmentNotExistException();
        }
        if(!permissionService.ifPermissionLevelSatisfied(userId, PermissionLevel.DISPLAY,department.getId())){
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
     * @param userId    the id of user
     * @param contactId the id of contact
     * @param eventId   the id of event
     */
    @Override
    public void deleteContact(Integer userId, Integer contactId, Integer eventId) {
        //check user
        checkUser(userId,eventId);

        QueryWrapper<Attend> wrapper = new QueryWrapper<>();
        wrapper.eq("contact_id",contactId)
                .eq("event_id",eventId);
        if(!attendService.remove(wrapper)){
            throw new FailToDeleteContactToEventException();
        }
    }
}
