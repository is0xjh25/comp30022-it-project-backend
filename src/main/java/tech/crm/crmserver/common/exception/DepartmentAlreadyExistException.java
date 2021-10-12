package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.constants.ExceptionMessageConstants;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The Exception When create permission but user already in the department
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-22
 */
public class DepartmentAlreadyExistException extends BaseException{

    public DepartmentAlreadyExistException() {
        super(ResponseResult.fail(ExceptionMessageConstants.DEPARTMENT_ALREADY_EXIST_EXCEPTION));
    }

}
