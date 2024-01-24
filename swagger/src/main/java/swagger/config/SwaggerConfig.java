package swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 老版本 swagger-ui 访问地址：http://localhost:8080/swagger-ui.html
 * 新版本 swagger-ui 访问地址：http://localhost:8080/doc.html
 * ----------------------------------------------------------------------
 * 实体类相关注解：
 * @ApiModel(value = "用户实体类", description = "用户实体类描述") --> 类注解
 * @ApiModelProperty(name = "username", value = "用户账号", example = "12345") --> 字段注解
 * @ApiModelProperty(name = "password", value = "用户密码", example = "12345") --> 字段注解
 * ----------------------------------------------------------------------
 * 控制层相关注解：
 * @Api(value = "用户数据操作类",tags = {"用户数据操作接口"}) --> 类注解
 * @ApiOperation(value = "删除数据", notes = "根据id删除用户数据") --> 方法注解
 * @ApiImplicitParam(name = "id", value = "用户id", required = true) --> 参数注解
 * ----------------------------------------------------------------------
 * @ApiImplicitParams({ --> 对象参数注解
 *         @ApiImplicitParam(name = "username", value = "用户名", required = true), --> 对象字段注解
 *         @ApiImplicitParam(name = "password", value = "用户密码", required = true) --> 对象字段注解
 * })
 * public ResponseResult putUserInfo(UserInfo userInfo){}
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApiConfig() {
        // 创建一个 swagger 的 bean 实例
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(webApiInfo()) // 指定构建 api 文档详细信息的方法
                .select() // 设置扫描接口
                .apis(RequestHandlerSelectors.basePackage("swagger.controller")) // 指定构建 api 接口的包路径,.any()为所有包
                .paths(PathSelectors.any()) // 可根据 url 路径设置那些请求加入文档，忽略哪些请求
                .build();
    }

    // api文档的详细信息
    private ApiInfo webApiInfo() {
        // 创建一个详细的主页说明
        return new ApiInfoBuilder()
                .title("API文档接口测试") // 标题
                .description("本文档描述接口测试用例") // 接口描述
                .version("1.0")  // 版本
                .contact(new Contact("岳培文", "www.baidu.com", "846692950@qq.com")) // 联系人-网址-邮箱
                .license("百度") // 证书描述
                .licenseUrl("www.baidu.com") // 证书地址
                .termsOfServiceUrl("www.baidu.com") // 系统服务网址
                .build();
    }

}