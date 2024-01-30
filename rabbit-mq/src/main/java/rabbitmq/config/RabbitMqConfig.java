package rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rabbitmq.util.MqConstant;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用 Jackson 来发送接收消息
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void init() {
        /**
         * 交换机不管是否收到消息的一个回调方法
         *
         * @param correlationData 保存回调信息的 Id 及相关信息
         * @param ack             交换机收到消息则为 true
         * @param cause           未收到消息的原因
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("--------------------------------------------------------------");
                System.out.println(correlationData.getId());
                System.out.println("ConfirmCallback     " + "相关数据：" + correlationData);
                System.out.println("ConfirmCallback     " + "确认情况：" + ack);
                System.out.println("ConfirmCallback     " + "原因：" + cause);
            }
        });

        /**
         * 当消息无法路由的时候的回调方法
         *
         * message      消息
         * replyCode    编码
         * replyText    退回原因
         * exchange     从哪个交换机退回
         * routingKey   通过哪个路由 key 退回
         */
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.println("--------------------------------------------------------------");
                System.out.println("ReturnCallback：     " + "消息：" + returned.getMessage().getBody());
                System.out.println("ReturnCallback：     " + "回应码：" + returned.getExchange());
                System.out.println("ReturnCallback：     " + "回应消息：" + returned.getReplyText());
                System.out.println("ReturnCallback：     " + "交换机：" + returned.getExchange());
                System.out.println("ReturnCallback：     " + "路由键：" + returned.getRoutingKey());
            }
        });
    }

    /**
     * 声明交换机
     * 参数1：交换机名称
     * 参数2：交换机类型
     * 参数3：指定交换机是否需要持久化，true即存到磁盘，false只在内存上
     * 参数4：指定交换机在沒有队列绑定时，是否删除？
     * 参数5：Map<String,Object>类型，用来指定我们交换机其他的一些结构化参数，我们在这里直接设置成Null
     *
     * exchangeDeclare(exchangeName, type, durable, autoDelete, arguments);
     */
    @Bean("directExchange")
    public DirectExchange directExchange() {
        return ExchangeBuilder
                .directExchange(MqConstant.DIRECT_EXCHANGE_NAME)
                .durable(false)
                .withArguments(new HashMap<>())
                .build();
    }

    @Bean("fanoutExchange")
    public FanoutExchange fanoutExchange() {
        return ExchangeBuilder
                .fanoutExchange(MqConstant.FANOUT_EXCHANGE_NAME)
                .durable(false)
                .withArguments(new HashMap<>())
                .build();
    }

    @Bean("topicExchange")
    public TopicExchange topicExchange() {
        return ExchangeBuilder
                .topicExchange(MqConstant.TOPIC_EXCHANGE_NAME)
                .durable(false)
                .withArguments(new HashMap<>())
                .build();
    }

    @Bean("deadExchange")
    public DirectExchange deadExchange() {
        return ExchangeBuilder
                .directExchange(MqConstant.DEAD_EXCHANGE_NAME)
                .durable(false)
                .withArguments(new HashMap<>())
                .build();
    }

    /**
     * 声明一个队列
     * 参数1：队列名称
     * 参数2：队列是否需要持久化，这里的持久化只是队列名称等这些源数据的持久化，不是队列中消息的持久化
     * 参数3：该队列是否只供一个消费者进行消费，是否进行消息共享，true：可以多个消费者消费 false：只能一个消费者消费
     * 参数4：队列在没有消费者订阅的情况下，是否自动删除
     * 参数5：Map<String,Object>类型，用来指定我们队列其他的一些结构化参数，比如声明死信队列
     *
     * new Queue(name, durable, exclusive, autoDelete, arguments);
     */
    @Bean("directQueue")
    public Queue directQueue() {
        //绑定死信队列并设置队列消息过期时间
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", MqConstant.DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", MqConstant.DEAD_KEY);
        return QueueBuilder
                .durable(MqConstant.DIRECT_QUEUE_NAME) //队列名称，并设置持久化
                .exclusive() //开启消息共享
                .withArguments(arguments) //其他的一些结构化参数
                .build();
    }

    @Bean("fanoutQueue")
    public Queue fanoutQueue() {
        //绑定死信队列并设置队列消息过期时间
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", MqConstant.DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", MqConstant.DEAD_KEY);
        return QueueBuilder
                .durable(MqConstant.FANOUT_QUEUE_NAME) //队列名称，并设置持久化
                .exclusive() //开启消息共享
                .withArguments(arguments) //其他的一些结构化参数
                .build();
    }

    @Bean("topicQueue")
    public Queue topicQueue() {
        //绑定死信队列并设置队列消息过期时间
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", MqConstant.DEAD_EXCHANGE_NAME);
        arguments.put("x-dead-letter-routing-key", MqConstant.DEAD_KEY);
        return QueueBuilder
                .durable(MqConstant.TOPIC_QUEUE_NAME) //队列名称，并设置持久化
                .exclusive() //开启消息共享
                .withArguments(arguments) //其他的一些结构化参数
                .build();
    }

    @Bean("deadQueue")
    public Queue deadQueue() {
        return QueueBuilder
                .durable(MqConstant.DEAD_QUEUE_NAME) //队列名称，并设置持久化
                .exclusive() //开启消息共享
                .withArguments(new HashMap<>()) //其他的一些结构化参数
                .ttl(10000) //设置消息过期时间（毫秒）
                .build();
    }

    /**
     * 将交换机与队列绑定
     * 参数1：队列名称
     * 参数2：交换机名称
     * 参数3：路由键
     */
    @Bean
    public Binding directQueueBinding(@Qualifier("directExchange") DirectExchange exchange,
                                      @Qualifier("directQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MqConstant.DIRECT_KEY);
    }

    @Bean
    public Binding fanoutQueueBinding(@Qualifier("fanoutExchange") FanoutExchange exchange,
                                      @Qualifier("fanoutQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange);
    }

    @Bean
    public Binding topicQueueBinding(@Qualifier("topicExchange") TopicExchange exchange,
                                     @Qualifier("topicQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MqConstant.TOPIC_KEY);
    }

    @Bean
    public Binding deadQueueBinding(@Qualifier("deadExchange") DirectExchange exchange,
                                    @Qualifier("deadQueue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(MqConstant.DEAD_KEY);
    }
}

