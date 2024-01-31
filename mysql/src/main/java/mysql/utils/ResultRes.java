package mysql.utils;

import lombok.Data;
import mysql.enums.ResultCode;

@Data
public class ResultRes<T> {

    private Integer code;
    private String message;
    private T data;

    private ResultRes() {
    }

    public static <T> ResultRes<T> build(T data, Integer code, String message) {
        ResultRes<T> result = new ResultRes<>();
        if (data != null) {
            result.setData(data);
        }
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static <T> ResultRes<T> build(T data, ResultCode resultCode) {
        ResultRes<T> result = new ResultRes<>();
        if (data != null) {
            result.setData(data);
        }
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        return result;
    }

    public static <T> ResultRes<T> ok(T data) {
        ResultRes<T> result = build(data, ResultCode.SUCCESS);
        return result;
    }

    public static <T> ResultRes<T> fail(T data) {
        return build(data, ResultCode.FAIL);
    }

}
