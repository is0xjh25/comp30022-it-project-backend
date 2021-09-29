package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.TodoListCreateDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  service
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
public interface ToDoListService extends IService<ToDoList> {

    /**
     * Get match todoList data based on condition
     * @param id the id of the todoList data
     * @param userId the user id of the todoList data
     * @param description the description of the todoList data
     * @param dateTime the due data of the todoList data
     * @param toDoListStatus the status of the todoList data
     * @return a list of match data
     */
    public List<ToDoList> queryTodoList(Integer id, Integer userId, String description, LocalDateTime dateTime, ToDoListStatus toDoListStatus);

    /**
     * Query all the todoList data of a user
     *
     * @param userId the id of the user to match
     * @param toDoListStatus the status to match
     * @return a list of todoList belongTo this user
     */
    public List<ToDoList> queryToDoListByUserId(Integer userId, ToDoListStatus toDoListStatus);

    /**
     * Query all the todoList data of a user
     *
     * @param userId the id of the user to match
     * @return a list of todoList belongTo this user
     */
    public List<ToDoList> queryToDoListByUserId(Integer userId);

    /**
     * Create todoList for a user
     *
     * @param userId the id of the user to match
     * @return if create success
     */
    public boolean createTodoList(Integer userId, TodoListCreateDTO todoListCreateDTO);
}
