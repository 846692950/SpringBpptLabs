package redis.controller;


import org.redisson.Redisson;
import org.redisson.RedissonRedLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ypw
 * @since 2024-01-23
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @GetMapping("/test")
    public String redisson() {
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
        return isLockBoolean ? "success" : "fail";
    }
}
