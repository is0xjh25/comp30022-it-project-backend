package tech.crm.crmserver.exception;

import org.springframework.http.HttpStatus;
import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When user no in department but still want to search member in department
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-22
 */
public class UserNotInDepartmentException extends BaseException{

    public UserNotInDepartmentException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_IN_DEPARTMENT_EXCEPTION));
    }

    public UserNotInDepartmentException(HttpStatus httpStatus) {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_IN_DEPARTMENT_EXCEPTION, httpStatus));
    }

}
