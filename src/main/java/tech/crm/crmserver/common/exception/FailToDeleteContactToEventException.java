package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When fail to delete contact to event
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-30
 */
public class FailToDeleteContactToEventException extends BaseException{

    public FailToDeleteContactToEventException() {
        super(ResponseResult.fail(ExceptionMessageConstants.FAIL_TO_DELETE_CONTACT_TO_EVENT_EXCEPTION));
    }

}
