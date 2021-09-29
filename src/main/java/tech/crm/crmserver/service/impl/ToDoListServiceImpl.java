package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.mapper.ToDoListMapper;
import tech.crm.crmserver.service.ToDoListService;

/**
 * <p>
 *  serviceImpl
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@Service
public class ToDoListServiceImpl extends ServiceImpl<ToDoListMapper, ToDoList> implements ToDoListService {

}
