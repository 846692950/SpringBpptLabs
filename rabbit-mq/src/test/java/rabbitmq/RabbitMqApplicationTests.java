package rabbitmq;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;
import rabbitmq.utils.MqConstant;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RabbitMqApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * 参数1：交换机
     * 参数2：路由键
     * 参数3：要发送的消息
     * 参数4：CorrelationData（id：封装相关性 id，returnedMessage：提供额外信息）
     */
    @Test
    void contextLoads() {
        //自定义CorrelationData，实现发布确认
        CorrelationData correlationData = new CorrelationData();
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Future发生异常时的处理逻辑，基本不会触发：" + ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                /**
                 * Future 接收到回执的处理逻辑，参数中的 result 就是回执内容
                 * result.isAck()，boolean 类型，true：代表 ack 回执，false：代表 nack 回执
                 * result.getReason()，String类型，返回 nack 时的异常描述
                 */
                if (result.isAck()) {
                    System.out.println("发送消息成功，收到 ack!");
                } else {
                    System.out.println("发送消息失败，收到 nack! 异常描述：{}" + result.getReason());
                }
            }
        });
        //需要发送的信息对象
        Map<String, Object> message = new HashMap<>();
        message.put("名字", "direct");
        message.put("描述", "这里是直连交换机");
        rabbitTemplate.convertAndSend(
                MqConstant.DIRECT_EXCHANGE_NAME,
                MqConstant.DIRECT_KEY,
                message,
                correlationData);

        rabbitTemplate.convertAndSend(
                MqConstant.FANOUT_EXCHANGE_NAME,
                null,
                "这里是扇出交换机！",
                new CorrelationData("fanout_2"));

        rabbitTemplate.convertAndSend(
                MqConstant.TOPIC_EXCHANGE_NAME,
                MqConstant.TOPIC_KEY,
                "这里是主题交换机！",
                new CorrelationData("topic_3"));

        rabbitTemplate.convertAndSend(
                MqConstant.DEAD_EXCHANGE_NAME,
                MqConstant.DEAD_KEY,
                "这里是死信交换机，信息存活20秒！",
                sss -> {
                    sss.getMessageProperties().setExpiration("20000");
                    return sss;
                },
                new CorrelationData("dead_4"));
    }

}
