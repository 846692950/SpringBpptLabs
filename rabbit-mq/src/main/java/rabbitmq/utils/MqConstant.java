package rabbitmq.utils;

/**
 * 常量类
 */
public final class MqConstant {
    /**
     * direct 交换机参数
     */
    public static final String DIRECT_EXCHANGE_NAME = "direct_exchange";
    public static final String DIRECT_QUEUE_NAME = "direct_queue";
    public static final String DIRECT_KEY = "direct_key";
    /**
     * fanout 交换机参数
     */
    public static final String FANOUT_EXCHANGE_NAME = "fanout_exchange";
    public static final String FANOUT_QUEUE_NAME = "fanout_queue";
    /**
     * topic 交换机参数
     *
     * topic 交换机的路由键不能随便写，有一定的匹配规则
     * *(星号)：可以代替一个位置
     * #(井号)：可以替代零个或多个位置
     *
     * Topic匹配案例：
     * (*.orange.*)：中间带 orange 带 3 个单词的字符串
     * (*.*.rabbit)：最后一个单词是 rabbit 的 3 个单词
     * (lazy.#)：第一个单词是 lazy 的多个单词
     */
    public static final String TOPIC_EXCHANGE_NAME = "topic_exchange";
    public static final String TOPIC_QUEUE_NAME = "topic_queue";
    public static final String TOPIC_KEY = "topic_key";

    /**
     * 死信交换机参数
     */
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";
    public static final String DEAD_QUEUE_NAME = "dead_queue";
    public static final String DEAD_KEY = "dead_key";
}
