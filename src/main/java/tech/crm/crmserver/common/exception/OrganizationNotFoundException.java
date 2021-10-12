package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When match organization cannot be found
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class OrganizationNotFoundException extends BaseException{

    public OrganizationNotFoundException() {
        super(ResponseResult.fail(ExceptionMessageConstants.ORGANIZATION_NOT_FOUND_EXCEPTION));
    }
}
