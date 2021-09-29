package tech.crm.crmserver.controller;


import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.crm.crmserver.common.enums.ToDoListStatus;
import tech.crm.crmserver.common.response.ResponseResult;
import tech.crm.crmserver.dao.ToDoList;
import tech.crm.crmserver.dto.TodoListCreateDTO;
import tech.crm.crmserver.dto.TodoListUpdateDTO;
import tech.crm.crmserver.exception.TodoListFailAddedException;
import tech.crm.crmserver.service.ToDoListService;
import tech.crm.crmserver.service.UserService;

import javax.validation.Valid;
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
        if (topNTodoListData == -1 && toDoList.size() > topNTodoListData) {
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
            throw new TodoListFailUpdateException();
        }
        return ResponseResult.suc("Update todolist success");
    }
}

