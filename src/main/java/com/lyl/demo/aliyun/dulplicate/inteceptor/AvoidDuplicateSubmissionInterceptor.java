package com.lyl.demo.aliyun.dulplicate.inteceptor;

import com.lyl.demo.aliyun.dulplicate.annotation.AvoidDuplicateSubmission;
import com.lyl.demo.aliyun.lock.CacheKeyGenerator;
import lombok.extern.slf4j.Slf4j;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * lyl 20180907
 * 防重提交拦截器
 *
 */
@Aspect
@Configuration
@Slf4j
public class AvoidDuplicateSubmissionInterceptor{

    private static final String prefix = "com:lyl";

    private final StringRedisTemplate lockRedisTemplate;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Autowired
    public AvoidDuplicateSubmissionInterceptor(StringRedisTemplate lockRedisTemplate, CacheKeyGenerator cacheKeyGenerator){
        this.lockRedisTemplate = lockRedisTemplate;
    }


    @Around("execution(public * *(..)) && @annotation(com.lyl.demo.aliyun.dulplicate.annotation.AvoidDuplicateSubmission)")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        //校验注解是否存在
        AvoidDuplicateSubmission avoidDuplicateSubmission = method.getAnnotation(AvoidDuplicateSubmission.class);
        if (StringUtils.isEmpty(avoidDuplicateSubmission)){
            throw new RuntimeException();
        }
        //如果两个注解同时为false 视为无效
        if (avoidDuplicateSubmission.needRemoveToken() == avoidDuplicateSubmission.needSaveToken()){
            return pjp.proceed();
        }

        //如果需要移除key值 表示用户表单提交成功
        if (avoidDuplicateSubmission.needRemoveToken()){
            if (lockRedisTemplate.delete(validateKey(request))){
                return pjp.proceed();
            }
            throw new RuntimeException("请勿重复提交表单");
        }

        //需要保存key值 通常用在提交表单，支付等插入数据库操作的前置操作
        if (avoidDuplicateSubmission.needSaveToken()){
            final String lockKey = generateKey(response);
            try {
                //采用原生API
                final Boolean success = lockRedisTemplate.execute(new RedisCallback<Boolean>() {
                    @Override
                    public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                        return connection.set(lockKey.getBytes(), new byte[0], Expiration.from(avoidDuplicateSubmission.expire(), avoidDuplicateSubmission.timeUnit()), RedisStringCommands.SetOption.SET_IF_ABSENT);
                    }
                });
                if (success){
                    return pjp.proceed();
                }
            } catch (Exception ex){
                log.error("redis系统异常", ex);
                return pjp.proceed();
            }
        }
        throw new RuntimeException("系统异常");
    }

    public String generateKey(HttpServletResponse response){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        String key = new StringBuffer()
                .append(prefix)
                .append(":")
                .append(token)
                .toString();
        response.setHeader("d-token", token);
        return key;
    }

    public String validateKey(HttpServletRequest request){
        String token = request.getHeader("d-token");
        return new StringBuffer()
                .append(prefix)
                .append(":")
                .append(token)
                .toString();
    }




}
