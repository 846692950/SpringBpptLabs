package mysql.controller;

import mysql.enums.ResultCode;
import mysql.exception.BizException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class exceptionTest {

    @GetMapping("/biz")
    public boolean test1() {
        throw new BizException(ResultCode.BIZ_EXCEPTION.getCode(), ResultCode.BIZ_EXCEPTION.getMessage());
    }

    @GetMapping("/other")
    public boolean test2() {
        int i = 1 / 0;
        return true;
    }
}
