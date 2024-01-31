package rabbitmq.example;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rabbitmq.utils.MqConstant;

import java.io.IOException;
import java.util.Map;

/**
 * 监听队列：当队列种有消息，则监听器工作，处理接收到的消息
 */
@Component
public class ListenerQueue {

    /**
     * 通过注解来设置交换机、队列，并绑定
     *
     * @RabbitListener(bindings = @QueueBinding(
     *         value = @Queue(name = MqConstant.TOPIC_QUEUE_NAME, durable = "true"),
     *         exchange = @Exchange(name = MqConstant.TOPIC_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
     *         key = {MqConstant.TOPIC_KEY,MqConstant.DIRECT_KEY}
     * ))
     */
    @RabbitListener(queues = MqConstant.DIRECT_QUEUE_NAME)
    public void process1(Map<String, Object> msg, Message message) throws IOException {
        System.out.println("接收到的direct消息-->:" + msg);
        System.out.println("接收到的direct消息-->:" + new String(message.getBody()));
    }

    @RabbitListener(queues = MqConstant.FANOUT_QUEUE_NAME)
    public void process2(Message message, Channel channel) throws IOException {
        byte[] body = message.getBody();
        System.out.println("接收到的fanout消息-->:" + new String(body));
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //肯定确认
        channel.basicAck(deliveryTag, false);

    }

    @RabbitListener(queues = MqConstant.TOPIC_QUEUE_NAME)
    public void process3(Message message, Channel channel) throws IOException {
        byte[] body = message.getBody();
        System.out.println("接收到的topic消息-->:" + new String(body));
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        //否定确认
        channel.basicNack(deliveryTag, false, false);
    }

    @RabbitListener(queues = MqConstant.DEAD_QUEUE_NAME)
    public void process4(Message message) {
        byte[] body = message.getBody();
        System.out.println("接收到的dead消息-->:" + new String(body));

    }
}

