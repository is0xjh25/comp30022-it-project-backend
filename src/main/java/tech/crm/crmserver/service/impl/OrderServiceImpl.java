package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.Order;
import tech.crm.crmserver.mapper.OrderMapper;
import tech.crm.crmserver.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-22
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

}
