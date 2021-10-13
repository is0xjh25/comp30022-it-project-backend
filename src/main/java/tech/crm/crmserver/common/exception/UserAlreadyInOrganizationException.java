package tech.crm.crmserver.common.exception;

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
public class UserAlreadyInOrganizationException extends BaseException{

    public UserAlreadyInOrganizationException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_ALREADY_IN_ORGANIZATION_EXCEPTION));
    }

}
