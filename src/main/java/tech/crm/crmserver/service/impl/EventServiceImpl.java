package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.mapper.EventMapper;
import tech.crm.crmserver.service.EventService;

import java.util.List;

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

    @Override
    public boolean createNewEvent(Integer userId, EventsDTO eventsDTO) {
        Event event = new Event();
        event.setUserId(userId);
        event.setStartTime(eventsDTO.getStartTime());
        event.setFinishTime(eventsDTO.getFinishTime());
        event.setDescription(eventsDTO.getDescription());
        return save(event);
    }
}
