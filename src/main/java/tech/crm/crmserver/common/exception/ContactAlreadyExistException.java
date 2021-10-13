package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When create new contact but the contact already exist
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class ContactAlreadyExistException extends BaseException{

    public ContactAlreadyExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.CONTACT_ALREADY_EXIST_EXCEPTION));
    }
}
