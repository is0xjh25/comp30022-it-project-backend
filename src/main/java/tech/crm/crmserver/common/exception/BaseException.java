package tech.crm.crmserver.common.exception;

import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The BaseException for all the RuntimeException in this program
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
public class BaseException extends RuntimeException{

    private ResponseResult<Object> responseResult;

    public BaseException(ResponseResult<Object> responseResult){
        this.responseResult = responseResult;
    }

    public ResponseResult<Object> getResponseResult() {
        return responseResult;
    }
}
