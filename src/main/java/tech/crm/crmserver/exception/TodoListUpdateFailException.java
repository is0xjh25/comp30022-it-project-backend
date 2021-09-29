package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

public class TodoListUpdateFailException extends BaseException {
    public TodoListUpdateFailException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_FAIL_ADDED_EXCEPTION));
    }
}
