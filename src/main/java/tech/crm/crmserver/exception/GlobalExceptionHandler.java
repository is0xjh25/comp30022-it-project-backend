package tech.crm.crmserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.crm.crmserver.common.response.ResponseResult;

/**
 * <p>
 *  The ControllerAdvice for all the Exception in this program
 *  base code from https://blog.csdn.net/qq_33249725/article/details/88308661
 * </p>
 *
 * @author Lingxiao
 * @since 2021-09-18
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * catch all BaseException and return the response
     * @param ex instance of BaseException
     * @return fail with msg
     */
    @ExceptionHandler(value =BaseException.class)
    public ResponseResult<Object> handleException(BaseException ex) {
        logger.warn("Exception Reason: " + ex.getResponseResult().getBody().getMsg());
        return ex.getResponseResult();
    }

    /**
     * catch all MethodArgumentNotValidException when the input dto is invalid
     * @param ex MethodArgumentNotValidException
     * @return fail with msg
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<Object> handleInvalidDTOException(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        logger.warn("Exception Reason: " + msg);
        return ResponseResult.fail(msg);
    }

}
