package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

public class ToDoListNotExistException extends BaseException {
    public ToDoListNotExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants))
    }
}
