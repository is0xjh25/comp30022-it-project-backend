package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.Event;
import tech.crm.crmserver.mapper.EventMapper;
import tech.crm.crmserver.service.EventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
