package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When can not delete contact from database
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class ContactFailDeleteException extends BaseException{

    public ContactFailDeleteException() {
        super(ResponseResult.fail(ExceptionMessageConstants.CONTACT_FAIL_DELETE_EXCEPTION));
    }
}
