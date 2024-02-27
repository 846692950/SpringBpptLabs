package swagger.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前端控制器
 *
 * @author ypw
 * @since 2024-01-23
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @ApiOperation("查询用户信息")
    @GetMapping("/getUser")
    public String page() {
        return "你好，你猜猜我是谁？";
    }

}
