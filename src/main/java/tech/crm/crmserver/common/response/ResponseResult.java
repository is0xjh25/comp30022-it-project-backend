package tech.crm.crmserver.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

/**
 * <p>
 *  Result: return the standard result with header and status
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-30
 * @param <T>
 */
public class ResponseResult<T> extends ResponseEntity<Result<T>> {

    public ResponseResult(Result<T> body, HttpStatus status) {
        super(body, status);
    }

    public ResponseResult(Result<T> body, MultiValueMap<String, String> headers, HttpStatus status) {
        super(body, headers, status);
    }


    /**
     * success, with msg
     */
    public static ResponseResult<Object> suc(String msg) {
        return new ResponseResult<Object>(Result.suc(msg),HttpStatus.OK);
    }

    /**
     * success, with msg and data
     */
    public static ResponseResult<Object> suc(String msg, Object data) {
        return new ResponseResult<Object>(Result.suc(msg,data),HttpStatus.OK);
    }

    /**
     * success, with msg, data and header
     */
    public static ResponseResult<Object> suc(String msg, Object data, MultiValueMap<String, String> headers) {
        return new ResponseResult<Object>(Result.suc(msg,data),headers,HttpStatus.OK);
    }


    /**
     * failure: specify the failure msg
     */
    public static ResponseResult<Object> fail(String msg) {
        return new ResponseResult<Object>(Result.fail(msg),HttpStatus.BAD_REQUEST);
    }

    /**
     * failure: specify the failure code and msg
     */
    public static ResponseResult<Object> fail(String msg, HttpStatus httpStatus) {
        return new ResponseResult<Object>(Result.fail(msg, httpStatus), httpStatus);
    }

}
