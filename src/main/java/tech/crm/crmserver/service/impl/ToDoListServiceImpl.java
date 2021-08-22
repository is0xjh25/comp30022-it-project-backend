package tech.crm.crmserver.service.impl;

import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.mapper.ToDoListMapper;
import tech.crm.crmserver.service.ToDoListService;
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
public class ToDoListServiceImpl extends ServiceImpl<ToDoListMapper, ToDoList> implements ToDoListService {

}
