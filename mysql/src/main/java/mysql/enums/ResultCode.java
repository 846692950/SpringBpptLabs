package mysql.enums;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),

    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),

    BIZ_EXCEPTION(666, "自定义异常"),
    UNKNOW_EXCEPTION(999, "网络不稳定");

    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
