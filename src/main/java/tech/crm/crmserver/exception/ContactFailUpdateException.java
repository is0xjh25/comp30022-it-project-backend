package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When can not update contact to database
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class ContactFailUpdateException extends BaseException{

    public ContactFailUpdateException() {
        super(ResponseResult.fail(ExceptionMessageConstants.CONTACT_FAIL_UPDATE_EXCEPTION));
    }
}
