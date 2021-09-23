package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When department do not exist
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-24
 */
public class DepartmentNotExistException extends BaseException{

    public DepartmentNotExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.DEPARTMENT_NOT_EXIST_EXCEPTION));
    }

}
