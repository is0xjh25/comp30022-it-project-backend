package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When fail to save organization into database
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class FailToCreateOrganizationException extends BaseException{

    public FailToCreateOrganizationException() {
        super(ResponseResult.fail(ExceptionMessageConstants.FAIL_TO_CREATE_ORGANIZATION_EXCEPTION));
    }
}
