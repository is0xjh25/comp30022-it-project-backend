package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When can not add todoList data into database
 * </p>
 *
 * @author Yongfeng Qin
 * @since 2021-09-28
 */
public class TodoListFailAddedException extends BaseException{

    public TodoListFailAddedException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_FAIL_ADDED_EXCEPTION));
    }
}
