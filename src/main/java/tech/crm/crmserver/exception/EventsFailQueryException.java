package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When can not add event into database
 * </p>
 *
 * @author Yongfeng Qin
 * @since 2021-09-30
 */
public class EventsFailQueryException extends BaseException{

    public EventsFailQueryException() {
        super(ResponseResult.fail(ExceptionMessageConstants.EVENT_FAIL_QUERY_EXCEPTION));
    }
}