package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When the update target todoList does not exist
 * </p>
 *
 * @author Kaiyuan Zheng
 * @since 2021-09-29
 */
public class ToDoListNotExistException extends BaseException {
    public ToDoListNotExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.TODOLIST_NOT_EXIST_EXCEPTION));
    }
}
