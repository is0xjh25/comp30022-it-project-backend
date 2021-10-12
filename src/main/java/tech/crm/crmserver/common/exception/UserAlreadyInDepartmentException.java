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
public class UserAlreadyInDepartmentException extends BaseException{

    public UserAlreadyInDepartmentException() {
        super(ResponseResult.fail(ExceptionMessageConstants.USER_ALREADY_IN_DEPARTMENT_EXCEPTION));
    }

}
