package tech.crm.crmserver.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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
@ControllerAdvice
@Slf4j
public class MyExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value =Exception.class)
    public ResponseResult<Object> handleException(Exception ex) {
        if (ex instanceof BaseException) {
            log.warn(ex.getMessage(), ex);
            BaseException baseException = (BaseException) ex;
            return baseException.getResponseResult();
        } else {
            log.error(ex.getMessage(), ex);
            return ResponseResult.fail(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
