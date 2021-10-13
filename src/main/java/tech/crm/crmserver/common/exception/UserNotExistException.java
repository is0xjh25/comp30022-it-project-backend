package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When The user not exist
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
public class UserNotExistException extends BaseException{

    public UserNotExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_EXIST_EXCEPTION));
    }
}
