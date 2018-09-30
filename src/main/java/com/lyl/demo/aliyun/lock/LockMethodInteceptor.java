package com.lyl.demo.aliyun.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 上午10:31
 */
@Aspect
@Configuration
public class LockMethodInteceptor {

    private final StringRedisTemplate lockRedisTemplate;
    private final CacheKeyGenerator cacheKeyGenerator;

    @Autowired
    public LockMethodInteceptor(StringRedisTemplate lockRedisTemplate, CacheKeyGenerator cacheKeyGenerator){
        this.lockRedisTemplate = lockRedisTemplate;
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    @Around("execution(public * *(..)) && @annotation(com.lyl.demo.aliyun.lock.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp){
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock)){
            throw new RuntimeException();
        }
        final String lockKey = cacheKeyGenerator.getLockKey(pjp);
        try {
            //采用原生API
            final Boolean success = lockRedisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                    return connection.set(lockKey.getBytes(), new byte[0], Expiration.from(lock.expire(), lock.timeUnit()), RedisStringCommands.SetOption.SET_IF_ABSENT);
                }
            });
            if (!success){
                throw new RuntimeException("请勿重复请求");
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable){
                throw new RuntimeException("系统异常");
            }

        } finally {
            lockRedisTemplate.delete(lockKey);
        }
    }

}
