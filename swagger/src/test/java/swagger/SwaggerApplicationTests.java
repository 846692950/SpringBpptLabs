package swagger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import swagger.entity.User;
import swagger.mapper.UserMapper;
import swagger.service.UserService;

@SpringBootTest
class SwaggerApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println("-----------------------");
        System.out.println(userMapper.selectById(1));
        System.out.println("-----------------------");
    }

}
