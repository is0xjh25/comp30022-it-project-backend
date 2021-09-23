package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When match contact cannot be found
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class ContactNotFoundException extends BaseException{

    public ContactNotFoundException() {
        super(ResponseResult.fail(ExceptionMessageConstants.CONTACT_NOT_FOUND_EXCEPTION));
    }
}
