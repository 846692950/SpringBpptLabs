package mysql.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description 自定义异常
 * @Date 2024-01-31 15:49
 * @Created ypw
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class BizException extends RuntimeException {
    /**
     * 错误码
     */
    protected Integer errorCode;
    /**
     * 错误信息
     */
    protected String errorMsg;
}