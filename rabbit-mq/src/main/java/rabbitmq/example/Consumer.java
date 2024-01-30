package rabbitmq.example;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.util.MQConnectionUtil;

/**
 * 消息接收者
 */
public class Consumer {
    public static void main(String[] args) throws Exception {

        //获取连接
        Connection connection = MQConnectionUtil.getConection();
        //创建channel
        Channel channel = connection.createChannel();

        //声明接收消息的回调函数
        DeliverCallback deliverCallback = (consumerTage, message) -> {
            System.out.println("接收到消息：" + new String(message.getBody()));
            System.out.println("------------<手动应答>的4个方法------------");
            /**
             * channel.basicAck (肯定确认)
             * 参数1：要肯定确认的消息的标识符
             * 参数2：是否批量确认？ true：表示批量确认信道中的消息 false：表示不批量确认信道中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

            /**
             * Channel.basicReject (否定确认)
             * 参数1：要否定确认的消息的标识符
             * 参数2：消息是否入队？ true：消息重新放回队列 false：消息从队列中删除或将其发送到死信队列
             */
            channel.basicReject(message.getEnvelope().getDeliveryTag(), false);

            /**
             * Channel.basicNack (否定确认)
             * 参数1：要否定确认的消息的标识符
             * 参数2：是否应用于多消息？ true：表示批量确认信道中的消息 false：表示不批量确认信道中的消息
             * 参数3：消息是否入队？ true：消息重新放回队列 false：消息从队列中删除或将其发送到死信队列
             */
            channel.basicNack(message.getEnvelope().getDeliveryTag(), false, false);

            /**
             * channel.basicRecover (恢复未确认的消息);
             * 参数1：是否恢复消息到队列? true：则重新入队列 false：则消息会重新被投递给自己
             */
            channel.basicRecover(true);

        };
        //取消消息时的回调函数
        CancelCallback cancelCallback = consumerTage -> {
            System.out.println("消息被中断");
        };

        /**
         * basicQos(num)：用于设置消费者从队列中获取消息的质量保证
         * 值为1：代表不公平分发，如果一个快一个慢，可以设置非公平分发，快的先走
         * 值为其他数字：代表通道缓冲区消息数量，只有信息少于设置的值，才会接收新消息
         */
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        /**
         * 消费者消费消息
         * 参数1：消费哪个队列
         * 参数2：消费成功之后是否要自动应答！true：代表自动应答 false：代表手动应答
         * 参数3：消费者未成功消费的回调
         * 参数4：消费者取消消费的回调
         */
        String queueName = "xc_queue_name";
        channel.basicConsume(queueName, false, deliverCallback, cancelCallback);

        channel.close();
        connection.close();

    }
}


