package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When The user email or password is not correct when login
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
public class LoginBadCredentialsException extends BaseException{

    public LoginBadCredentialsException() {
        super(ResponseResult.fail(ExceptionMessageConstants.LOGIN_BAD_CREDENTIALS_EXCEPTION));
    }
}
