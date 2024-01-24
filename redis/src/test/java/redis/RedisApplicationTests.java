package redis;

import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import redis.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisApplicationTests {

    @Resource
    private RedisUtil redisUtil;

    /**
     * redis 缓存
     */
    @Test
    void redisCache() {
        //存入Redis
        redisUtil.set("username", "yuepeiwen");
        System.out.println("保存成功！！！");
        //根据key取出
        String username = (String) redisUtil.get("username");
        System.out.println("username=" + username);
    }

    /**
     * redisson 分布式锁的模拟使用
     */
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                redisson();
            }).start();
        }
    }

    /**
     * redisson 分布式锁
     */
    @Test
    static void redisson() {
        Config config1 = new Config();
        config1.useSingleServer()
                .setAddress("redis://43.142.106.101:6379")
                .setDatabase(11);
        RedissonClient redissonClient1 = Redisson.create(config1);

        Config config2 = new Config();
        config2.useSingleServer()
                .setAddress("redis://43.142.106.101:6379")
                .setDatabase(12);
        RedissonClient redissonClient2 = Redisson.create(config2);

        Config config3 = new Config();
        config3.useSingleServer()
                .setAddress("redis://43.142.106.101:6379")
                .setDatabase(13);
        RedissonClient redissonClient3 = Redisson.create(config3);

        //获取多个 RLock 对象
        final String lockKey = "MyLock";
        RLock lock1 = redissonClient1.getLock(lockKey);
        RLock lock2 = redissonClient2.getLock(lockKey);
        RLock lock3 = redissonClient3.getLock(lockKey);

        //根据多个 RLock 对象构建 RedissonRedLock （最核心的差别就在这里）
        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);

        //定义获取锁标志位,默认是获取失败
        boolean isLockBoolean = false;
        try {
            //尝试5秒内获取锁，如果获取到了，最长60秒自动释放
            isLockBoolean = redLock.tryLock(5L, 60L, TimeUnit.SECONDS);
            System.out.printf("线程:" + Thread.currentThread().getId() + ",是否拿到锁:" + isLockBoolean + "\n");
            if (isLockBoolean) {
                System.out.println("线程:" + Thread.currentThread().getId() + ",加锁成功，进入业务操作");
                try {
                    //业务逻辑，40s模拟，超过了key的过期时间
                    TimeUnit.SECONDS.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            System.out.println("获取锁失败：" + e.getMessage());
        } finally {
            //无论如何, 最后都要解锁
            redLock.unlock();
        }
        System.out.println(isLockBoolean ? "success" : "fail");
    }

}
