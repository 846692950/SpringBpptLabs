package redis.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Description Redis切面类
 * @Author IT ypw
 * @Date 2023/05/19
 */
@Slf4j
public class RedisAspect {
    /**
     * @Pointcut("execution(* redis.utils.*(..))"): 这是一个切入点（pointcut），它定义了在哪些方法上应用切面
     * public void pointcut() { }: 这是一个空方法，实际上仅仅是用来命名切入点的，因为 @Pointcut 注解需要标记在一个方法上。
     */
    @Pointcut("execution(* redis.utils.*(..))")
    public void pointcut() {
    }

    /**
     * @Around("pointcut()"): 这是一个环绕通知（around advice），它告诉 Spring 在切入点的周围执行额外的逻辑。
     * ("pointcut()"): 这个括号内的字符串是一个表达式，用来匹配切入点。
     */
    @Around("pointcut()")
    public Object handleException(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error("Redis似乎出现了某些不可违因素");
        }
        return result;
    }

}

