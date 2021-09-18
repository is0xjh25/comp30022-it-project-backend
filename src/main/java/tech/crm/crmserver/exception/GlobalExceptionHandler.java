package tech.crm.crmserver.exception;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value =BaseException.class)
    public ResponseResult<Object> handleException(Exception ex) {
        log.warn(ex.getMessage(), ex);
        BaseException baseException = (BaseException) ex;
        return baseException.getResponseResult();
    }

}
