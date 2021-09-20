package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When The user email or password is not correct when login
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
public class UserAlreadyExistException extends BaseException{

    public UserAlreadyExistException() {
        super(ResponseResult.fail("Same email already exist!"));
    }
}
