package rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MQConnectionUtil {
    /**
     * 获得Rabbit的连接
     * 类比JDBC
     */
    public static Connection getConection() throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置的rabbitMq的服务的主机，默认localhost
        factory.setHost("43.142.106.101");
        //设置的rabbitMq的服务的端口，默认为5672
        factory.setPort(5672);
        //用户名
        factory.setUsername("guest");
        //密码
        factory.setPassword("guest");
        //创建连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
