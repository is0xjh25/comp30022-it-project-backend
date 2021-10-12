package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When contact do not exist
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class ContactNotExistException extends BaseException{

    public ContactNotExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.CONTACT_NOT_EXIST_EXCEPTION));
    }
}
