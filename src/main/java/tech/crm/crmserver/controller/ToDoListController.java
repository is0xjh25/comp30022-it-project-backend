package tech.crm.crmserver.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.exception.TodoListDeleteFailException;
import tech.crm.crmserver.common.exception.TodoListFailAddedException;
import tech.crm.crmserver.common.exception.TodoListUpdateFailException;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.TaskStatDTO;
import tech.crm.crmserver.dto.TodoListCreateDTO;
import tech.crm.crmserver.dto.TodoListUpdateDTO;
import tech.crm.crmserver.service.ToDoListService;
import tech.crm.crmserver.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 *  controller
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-23
 */
@RestController
@RequestMapping("/toDoList")
public class ToDoListController {

    @Autowired
    private ToDoListService toDoListService;

    @Autowired
    private UserService userService;

    /**
     * Get all todoList data for a user
     *
     * @param topNTodoListData the first n todoList data
     * @return ResponseResult contain all the data about user's todoList
     */
    @GetMapping
    public ResponseResult<Object> getAllTodoListData(@RequestParam("topNTodoListData") Integer topNTodoListData) {
        Integer userId = userService.getId();
        List<ToDoList> toDoList = toDoListService.queryToDoListByUserId(userId);
        toDoList.sort(new Comparator<ToDoList>() {
            @Override
            public int compare(ToDoList o1, ToDoList o2) {
                if (o1.getDateTime().isBefore(o2.getDateTime())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        if (topNTodoListData != -1 && toDoList.size() > topNTodoListData) {
            toDoList.subList(0, topNTodoListData);
        }
        return ResponseResult.suc("Query user's todoList", toDoList);
    }

    /**
     * Create new todoList data for a user
     *
     * @param todoListCreateDTO the data of the todoList to create
     * @return ResponseResult contain if the creation is a success
     */
    @PostMapping
    public ResponseResult<Object> createTodoList(@RequestBody @Valid TodoListCreateDTO todoListCreateDTO) {
        Integer userId = userService.getId();
        boolean isCreateSuccess = false;
        isCreateSuccess = toDoListService.createTodoList(userId, todoListCreateDTO);
        if (!isCreateSuccess) {
            throw new TodoListFailAddedException();
        }
        return ResponseResult.suc("Adding todoList data success");
    }

    /**
     * Update an existing todolist for a user
     *
     * @param todoListDTO the details of the updated todolist
     * @return ResponseResult about if the update succeeds, or why it fails
     */
    @PutMapping
    public ResponseResult<Object> updateTodoList(@RequestBody @Valid TodoListUpdateDTO todoListDTO) {
        boolean updateSuccess = false;
        updateSuccess = toDoListService.updateTodoListByTodoListDTO(todoListDTO, userService.getId());
        if (!updateSuccess) {
            throw new TodoListUpdateFailException();
        }
        return ResponseResult.suc("Update todolist success");
    }

    /**
     * Delete an existing todolist for a user
     *
     * @param todoListId the todoList to be deleted
     * @return ResponseResult about if delete is successful, or why it fails
     */
    @DeleteMapping
    public ResponseResult<Object> deleteTodoList(@RequestParam("todoList_id") Integer todoListId) {
        boolean deleteSuccess = false;
        deleteSuccess = toDoListService.deleteTodoListByTodoListId(todoListId, userService.getId());
        if (!deleteSuccess) {
            throw new TodoListDeleteFailException();
        }
        return ResponseResult.suc("Delete todolist success");
    }

    /**
     * get statistic information of to do list for this user
     * @return ResponseResult contain the start time list of to do lists of this user
     */
    @GetMapping("/statistic")
    public ResponseResult<Object> getStat(@RequestParam("start_time") LocalDateTime startTime,
                                          @RequestParam("finish_time")LocalDateTime finishTime){
        TaskStatDTO stat = toDoListService.getStat(userService.getId(), startTime, finishTime);
        return ResponseResult.suc("Successfully get the statistic information", stat);
    }
}

