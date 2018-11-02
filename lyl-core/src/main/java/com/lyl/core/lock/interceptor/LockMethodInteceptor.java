package com.lyl.core.lock.interceptor;

import com.lyl.core.enable.RedisTemplate;
import com.lyl.core.lock.annotation.CacheLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 上午10:31
 */
@Aspect
public class LockMethodInteceptor {

    private RedisTemplate redisTemplate;

    private HttpServletRequest request;

    @Autowired
    public LockMethodInteceptor(RedisTemplate redisTemplate, HttpServletRequest request){
        this.redisTemplate = redisTemplate;
        this.request = request;
    }

    @Around("execution(public * *(..)) && @annotation(com.lyl.core.lock.annotation.CacheLock)")
    public Object interceptor(ProceedingJoinPoint pjp){

        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        CacheLock lock = method.getAnnotation(CacheLock.class);
        if (StringUtils.isEmpty(lock)){
            throw new RuntimeException();
        }
        //获取token
        String token = request.getParameter("token");
        if (StringUtils.isEmpty(token)){
            return false;
        }
        //redis key
        try {
            final Boolean success = redisTemplate.set(buildKey(token), "", lock.expire());
            if (!success){
                System.out.println("请勿重复请求");
                return false;
            }
            try {
                return pjp.proceed();
            } catch (Throwable throwable){
                throw new RuntimeException("系统异常");
            }

        } finally {
//            redisTemplate.delete(lockKey);
        }
    }

    private String buildKey(String token){
        return new StringBuilder()
                .append("avoid:submit")
                .append(":")
                .append(request.getRequestURI())
                .append(":")
                .append(token)
                .toString();

    }

}
