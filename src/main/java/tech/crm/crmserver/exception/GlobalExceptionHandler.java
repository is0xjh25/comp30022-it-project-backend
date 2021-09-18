package tech.crm.crmserver.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @ExceptionHandler(value =BaseException.class)
    public ResponseResult<Object> handleException(BaseException ex) {
        logger.warn("Exception Reason: " + ex.getResponseResult().getBody().getMsg());
        return ex.getResponseResult();
    }

}
