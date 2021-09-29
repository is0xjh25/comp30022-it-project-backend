package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When deleting a todoList fails
 * </p>
 *
 * @author Kaiyuan Zheng
 * @since 2021-09-29
 */
public class TodoListDeleteFailException extends BaseException {
    public TodoListDeleteFailException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_DELETE_FAIL_EXCEPTION));
    }
}
