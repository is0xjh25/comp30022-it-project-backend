package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When fail to save organization into database
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-25
 */
public class IdNotExistException extends BaseException{

    public IdNotExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.ID_NOT_EXIST_EXCEPTION));
    }

}
