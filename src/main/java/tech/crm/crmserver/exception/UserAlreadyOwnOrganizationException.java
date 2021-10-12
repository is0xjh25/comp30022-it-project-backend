package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When create belong_to but user already in the organization
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class UserAlreadyOwnOrganizationException extends BaseException{

    public UserAlreadyOwnOrganizationException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_ALREADY_OWN_ORGANIZATION_EXCEPTION));
    }

}
