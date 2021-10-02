package tech.crm.crmserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.TaskStatDTO;
import tech.crm.crmserver.dto.TodoListCreateDTO;
import tech.crm.crmserver.dto.TodoListUpdateDTO;

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
     * @param startTime the start data of the todoList data
     * @param finishTime the due data of the todoList data
     * @param toDoListStatus the status of the todoList data
     * @return a list of match data
     */
    public List<ToDoList> queryTodoList(Integer id,
                                        Integer userId,
                                        String description,
                                        LocalDateTime startTime,
                                        LocalDateTime finishTime,
                                        ToDoListStatus toDoListStatus);

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

    /**
     * Update todolist based on todolistUpdateDTO
     *
     * @param todoListDTO all the information that the update needs
     * @param userId the user who wants to update the todolist
     * @return if the update is successful or not
     */
    public boolean updateTodoListByTodoListDTO(TodoListUpdateDTO todoListDTO, Integer userId);

    /**
     * Transfer from TodoListUpdateDTO to a todolist object
     *
     * @param todoListUpdateDTO the TodoListUpdateDTO to transfer
     * @return a todolist object
     */
    public ToDoList fromTodoListUpdateDTO(TodoListUpdateDTO todoListUpdateDTO);

    /**
     * Update a todoList
     *
     * @param todoList the todoList to update
     * @return if the update is successful
     */
    public boolean updateTodoList(ToDoList todoList);

    /**
     * Delete a todoList
     *
     * @param todoListId the todolist to be deleted
     * @param userId the user who wants to delete the todolist
     * @return if the todolist is deleted successfully
     */
    public boolean deleteTodoListByTodoListId(Integer todoListId, Integer userId);

    /**
     * return the statistic information
     * @param userId the id of user
     * @param startTime start of given time
     * @param finishTime end of given time
     * @return the statistic information
     */
    public TaskStatDTO getStat(@Param("userId") Integer userId, LocalDateTime startTime, LocalDateTime finishTime);
}
