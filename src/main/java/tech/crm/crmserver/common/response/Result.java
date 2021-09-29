package tech.crm.crmserver.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * <p>
 *  Result: return the standard result
 * </p>
 *
 * @author Lingxiao
 * @since 2021-08-30
 * @param <T> the data of response
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Result<T> {

    private Integer code;
    private String status;
    private String msg;
    private T data;


    /**
     * success, with msg
     */
    public static Result<Object> suc(String msg) {
        Result<Object> result = new Result<>();
        result.setHttpStatus(HttpStatus.OK);
        result.setMsg(msg);
        return result;
    }

    /**
     * success, with msg and data
     */
    public static Result<Object> suc(String msg, Object data) {
        Result<Object> result = new Result<>();
        result.setHttpStatus(HttpStatus.OK);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }



    /**
     * failure: specify the failure msg
     */
    public static Result<Object> fail(String msg) {
        Result<Object> result = new Result<>();
        result.setHttpStatus(HttpStatus.BAD_REQUEST);
        result.setMsg(msg);
        return result;
    }

    /**
     * failure: specify the failure code and msg
     */
    public static Result<Object> fail(String msg, HttpStatus httpStatus) {
        Result<Object> result = new Result<>();
        result.setHttpStatus(httpStatus);
        result.setMsg(msg);
        return result;
    }

    /**
     * set code and status according to httpStatus
     */
    private void setHttpStatus(HttpStatus httpStatus){
        this.code = httpStatus.value();
        this.status = httpStatus.getReasonPhrase();
    }
}
