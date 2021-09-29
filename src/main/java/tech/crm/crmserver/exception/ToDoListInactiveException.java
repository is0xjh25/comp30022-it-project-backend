package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When the update target todoList is inactive
 * </p>
 *
 * @author Kaiyuan
 * @since 2021-09-29
 */
public class ToDoListInactiveException extends BaseException {
    public ToDoListInactiveException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_INACTIVE_EXCEPTION));
    }
}
