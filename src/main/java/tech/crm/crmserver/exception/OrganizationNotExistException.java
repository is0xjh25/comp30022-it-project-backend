package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When Organization do not exist
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-21
 */
public class OrganizationNotExistException extends BaseException{

    public OrganizationNotExistException() {
        super(ResponseResult.fail("Organization do not exist!"));
    }
}
