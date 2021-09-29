package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When can not update todoList to database
 * </p>
 *
 * @author Kaiyuan
 * @since 2021-09-29
 */
public class TodoListUpdateFailException extends BaseException {
    public TodoListUpdateFailException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_FAIL_ADDED_EXCEPTION));
    }
}
