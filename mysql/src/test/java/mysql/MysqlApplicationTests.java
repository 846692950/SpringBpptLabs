package mysql;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@SpringBootTest
class MysqlApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 通过sql语句来测试数据库是否能够正常使用
     */
    @Test
    void contextLoads() {
        String sql = "select name from user";
        List<String> lists = jdbcTemplate.queryForList(sql, String.class);
        System.out.println("--------------------------------------------");
        for (String s : lists) {
            System.out.println(s);
        }
    }

}
