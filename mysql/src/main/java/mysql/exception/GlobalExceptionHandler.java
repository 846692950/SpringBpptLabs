package mysql.exception;

import lombok.extern.slf4j.Slf4j;
import mysql.enums.ResultCode;
import mysql.utils.ResultRes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 全局异常处理器
 * @Date 2024-01-31 15:49
 * @Created ypw
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     */
    @ExceptionHandler(value = BizException.class)
    public ResultRes bizExceptionHandler(HttpServletRequest req, BizException e) {
        log.error("发生自定义异常！原因是：{}", e.getErrorMsg());
        return ResultRes.build(null, e.getErrorCode(), e.getErrorMsg());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(value = Exception.class)
    public ResultRes exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("发生未知异常！原因是：{}", e.getMessage());
        return ResultRes.build(null, ResultCode.UNKNOW_EXCEPTION);
    }
}
