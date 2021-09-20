package tech.crm.crmserver.exception;

import org.springframework.http.HttpStatus;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When The user do not have enough permission to do the action
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-21
 */
public class NotEnoughPermissionException extends BaseException{

    public NotEnoughPermissionException() {
        super(ResponseResult.fail("Sorry you do not have enough permissions to access it!", HttpStatus.FORBIDDEN));
    }
}
