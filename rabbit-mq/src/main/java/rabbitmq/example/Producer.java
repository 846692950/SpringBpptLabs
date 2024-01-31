package rabbitmq.example;

import com.rabbitmq.client.*;
import rabbitmq.utils.MQConnectionUtil;
import rabbitmq.utils.MqConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息生产者
 */
public class Producer {
    public static void main(String[] args) throws Exception {

        //获取连接
        Connection connection = MQConnectionUtil.getConection();
        //创建信道channel
        Channel channel = connection.createChannel();

        /**
         * 创建交换机
         * 参数1：交换机名称
         * 参数2：交换机类型 direct, topic, fanout, headers
         * 参数3：指定交换机是否需要持久化，true即存到磁盘，false只在内存上
         * 参数4：指定交换机在沒有队列绑定时，是否删除？
         * 参数5：Map<String,Object>类型，用来指定我们交换机其他的一些结构化参数，我们在这里直接设置成Null
         */
        String exchangeName = "xc_exchange_name";
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true, false, null);
        /**
         * 创建一个死信交换机
         */
        channel.exchangeDeclare(MqConstant.DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, null);

        /**
         * 声明一个队列 并绑定死信队列
         * 参数1：队列名称
         * 参数2：队列是否需要持久化，这里的持久化只是队列名称等这些源数据的持久化，不是队列中消息的持久化
         * 参数3：该队列是否只供一个消费者进行消费，是否进行消息共享，true：可以多个消费者消费 false：只能一个消费者消费
         * 参数4：队列在没有消费者订阅的情况下，是否自动删除
         * 参数5：Map<String,Object>类型，用来指定我们队列其他的一些结构化参数，比如声明死信队列
         */
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", MqConstant.DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", MqConstant.DEAD_KEY);
        String queueName = "xc_queue_name";
        channel.queueDeclare(queueName, false, false, false, arguments);
        /**
         * 声明一个死信队列
         */
        channel.queueDeclare(MqConstant.DEAD_QUEUE_NAME, false, false, false, null);

        /**
         * 将交换机与队列绑定
         * 参数1：队列名称
         * 参数2：交换机名称
         * 参数3：路由鍵，在我们直连模式下，可以为我们的队列名称
         */
        channel.queueBind(queueName, exchangeName, queueName);
        /**
         * 将死信交换机和死信队列绑定
         */
        channel.queueBind(MqConstant.DEAD_QUEUE_NAME, MqConstant.DEAD_EXCHANGE_NAME, MqConstant.DEAD_KEY);

        /**
         * 开启发布确认：confirmSelect()
         */
        channel.confirmSelect();

        /**
         * 消息确认成功回调的函数
         * 参数1：消息的标识符
         * 参数2：是否为批量确认
         */
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息:" + deliveryTag);
        };

        /**
         * 消息确认失败回调函数
         * 参数1：消息的标识符
         * 参数2：是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息:" + deliveryTag);
        };

        /**
         * 消息监听器：异步通知，监听哪些消息成功了，哪些消息失败了
         * 1.监听哪些消息成功了
         * 2.监听哪些消息失败了
         */
        channel.addConfirmListener(ackCallback, nackCallback);

        /**
         * 发送消息
         * 参数1：发送到哪个交换机
         * 参数2：路由键，在这里是队列名称
         * 参数3：可选参数，允许你设置消息的各种属性，如持久性、优先级、消息ID、时间戳等
         * 参数4：发送的消息体
         */
        String ms = "Hello rabbitMQ";
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder()
                .expiration("10000") //TTL 10秒
                .deliveryMode(1) //持久化消息 1：非持久化 2：持久化
                .contentEncoding("UTF-8")
                .build();
        channel.basicPublish(exchangeName, queueName, properties, ms.getBytes());

        /**
         * 获取消息发送成功后的确认值：waitForConfirms()
         * 单个发布确认：每发送一条消息就接收一个确认值
         * 批量发布确认：发送指定数量的消息后，如100条消息发完后接收一次确认值
         * 异步发布确认：通过上边的发布确认回调函数实现
         */
        boolean flag = channel.waitForConfirms();
        if (flag) {
            System.out.println("消息发送成功");
        }

        channel.close();
        connection.close();

    }
}

