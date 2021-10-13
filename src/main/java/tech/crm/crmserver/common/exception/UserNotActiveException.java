package tech.crm.crmserver.common.exception;

import org.springframework.http.HttpStatus;
import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When The user is not active(is pending)
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-28
 */
public class UserNotActiveException extends BaseException{

    public UserNotActiveException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_ACTIVE_EXCEPTION));
    }

    public UserNotActiveException(HttpStatus httpStatus) {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_ACTIVE_EXCEPTION, httpStatus));
    }

}
