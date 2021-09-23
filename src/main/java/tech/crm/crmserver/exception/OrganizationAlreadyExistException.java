package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When Organization already not exist
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class OrganizationAlreadyExistException extends BaseException{

    public OrganizationAlreadyExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.ORGANIZATION_ALREADY_EXIST_EXCEPTION));
    }
}
