package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When userPhoto is null
 * </p>
 *
 * @author Lingxiao
 * @since 2021-10-10
 */
public class BadPhotoException extends BaseException{

    public BadPhotoException() {
        super(ResponseResult.fail(ExceptionMessageConstants.BAD_PHOTO_EXCEPTION));
    }

}
