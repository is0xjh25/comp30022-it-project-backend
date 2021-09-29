package tech.crm.crmserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.TodoListCreateDTO;
import tech.crm.crmserver.mapper.ToDoListMapper;
import tech.crm.crmserver.service.ToDoListService;

import java.time.LocalDateTime;
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
public class ToDoListServiceImpl extends ServiceImpl<ToDoListMapper, ToDoList> implements ToDoListService {

    @Autowired
    private ToDoListMapper toDoListMapper;


    /**
     * Get match todoList data based on condition
     * @param id the id of the todoList data
     * @param userId the user id of the todoList data
     * @param description the description of the todoList data
     * @param dateTime the due data of the todoList data
     * @param toDoListStatus the status of the todoList data
     * @return a list of match data
     */
    @Override
    public List<ToDoList> queryTodoList(Integer id, Integer userId, String description, LocalDateTime dateTime, ToDoListStatus toDoListStatus) {
        QueryWrapper<ToDoList> queryWrapper = new QueryWrapper<>();
        if (id != null) {
            queryWrapper.eq("id", id);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (description != null) {
            queryWrapper.eq("date_time", dateTime);
        }
        if (toDoListStatus != null) {
            queryWrapper.eq("status", toDoListStatus.getStatus());
        }

        return toDoListMapper.selectList(queryWrapper);
    }

    /**
     * Query all the todoList data of a user
     *
     * @param userId the id of the user to match
     * @param toDoListStatus the status to match
     * @return a list of todoList belongTo this user
     */
    @Override
    public List<ToDoList> queryToDoListByUserId(Integer userId, ToDoListStatus toDoListStatus) {
        QueryWrapper<ToDoList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("status", toDoListStatus.getStatus());
        return toDoListMapper.selectList(queryWrapper);
    }

    @Override
    public boolean createTodoList(Integer userId, TodoListCreateDTO todoListCreateDTO) {
        ToDoList toDoList = new ToDoList();
        toDoList.setUserId(userId);
        toDoList.setDateTime(todoListCreateDTO.getDateTime());
        toDoList.setDescription(todoListCreateDTO.getDescription());
        toDoList.setStatus(todoListCreateDTO.getStatus());
        return save(toDoList);
    }
}
