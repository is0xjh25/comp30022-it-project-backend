package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

public class ToDoListInactiveException extends BaseException {
    public ToDoListInactiveException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_INACTIVE_EXCEPTION));
    }
}
