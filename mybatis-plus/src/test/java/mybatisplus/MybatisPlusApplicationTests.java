package mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import mybatisplus.entity.User;
import mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MybatisPlusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void test1() {
        // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNotNull("name")
                .isNotNull("age")
                .ge("id", 2)
        ;
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test2() {
        // 查询名字张三
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "张三");
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    @Test
    void test3() {
        // 查询年龄在 20 ~ 30 岁之间的用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.between("age", 20, 30); // 区间
        userMapper.selectList(wrapper).forEach(System.out::println);
    }

    @Test
    void test4() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // id 在子查询中查出来
        wrapper.inSql("id", "select id from user where id < 3");
        List<Object> objects = userMapper.selectObjs(wrapper);
        objects.forEach(System.out::println);
    }

    @Test
    void test5() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 通过id进行排序
        wrapper.orderByAsc("id");
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(System.out::println);
    }

    @Test
    void test6() {
        User user = new User();
        user.setName("靓仔");
        user.setAge(24);
        user.setEmail("846692950@qq.com");
        userMapper.insert(user);
    }

    @Test
    void test7() {
        User user = new User();
        user.setId(10);
        userMapper.deleteById(user);
    }

    @Test
    void test8() {
        User user = new User();
        user.setId(11);
        user.setName("东杰");
        user.setAge(24);
        user.setEmail("846692950@qq.com");
        userMapper.updateById(user);
    }

}
