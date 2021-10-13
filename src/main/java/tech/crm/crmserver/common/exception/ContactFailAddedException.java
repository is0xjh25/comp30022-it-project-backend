package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When can not add contact to database
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class ContactFailAddedException extends BaseException{

    public ContactFailAddedException() {
        super(ResponseResult.fail(ExceptionMessageConstants.CONTACT_FAIL_ADDED_EXCEPTION));
    }
}
