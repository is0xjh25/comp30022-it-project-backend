package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.constants.EmailConstants;
import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.dao.User;
import tech.crm.crmserver.dto.EventsDTO;
import tech.crm.crmserver.dto.EventsUpdateDTO;
import tech.crm.crmserver.exception.NotEnoughPermissionException;
import tech.crm.crmserver.mapper.EventMapper;
import tech.crm.crmserver.service.AttendService;
import tech.crm.crmserver.service.EventService;

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

    /**
     * update the information of a event
     *
     * @param userId          the user of this event
     * @param eventsUpdateDTO the update information
     */
    @Override
    public void updateEvent(Integer userId, EventsUpdateDTO eventsUpdateDTO) {
        //check user
        Event event = baseMapper.selectById(eventsUpdateDTO.getId());
        if(!event.getUserId().equals(userId)){
            throw new NotEnoughPermissionException();
        }

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

    @Override
    public void deleteEvent(Integer eventId, Integer userId) {
        //check user
        Event event = baseMapper.selectById(eventId);
        if(!event.getUserId().equals(userId)){
            throw new NotEnoughPermissionException();
        }

        attendService.deleteAttendByEventId(eventId);
        removeById(eventId);
    }
}
