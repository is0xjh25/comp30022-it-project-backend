package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;

import java.util.List;

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

}
