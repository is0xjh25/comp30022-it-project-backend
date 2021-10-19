package tech.crm.crmserver.common.exception;

import org.springframework.http.HttpStatus;
import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When user no in organization
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-22
 */
public class UserNotInOrganizationException extends BaseException{

    public UserNotInOrganizationException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_IN_ORGANIZATION_EXCEPTION));
    }

    public UserNotInOrganizationException(HttpStatus httpStatus) {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_NOT_IN_ORGANIZATION_EXCEPTION, httpStatus));
    }

}
