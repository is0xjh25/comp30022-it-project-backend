package tech.crm.crmserver.exception;

import tech.crm.crmserver.common.response.ResponseResult;

public class UserAlreadyInDepartmentException extends BaseException{

    public UserAlreadyInDepartmentException() {
        super(ResponseResult.fail("This user already in the department"));
    }
}
