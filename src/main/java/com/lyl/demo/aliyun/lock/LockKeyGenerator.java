package com.lyl.demo.aliyun.lock;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @Author lyl
 * @Description 通过接口注入的方式去写不同的生成规则;
 * @Date 2018/9/30 上午10:21
 */
@Component
public class LockKeyGenerator implements CacheKeyGenerator {


    @Override
    public String getLockKey(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        CacheLock localAnnotation = method.getAnnotation(CacheLock.class);
        final Object[] args = pjp.getArgs();
        final Parameter[] parameters = method.getParameters();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parameters.length; i++){
            final CacheParam annotation = parameters[i].getAnnotation(CacheParam.class);
            if (annotation == null){
                continue;
            }
            builder.append(localAnnotation.delimiter()).append(args[i]);
        }
        if (StringUtils.isEmpty(builder.toString())){
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++){
                final Object object = args[i];
                final Field[] fields = object.getClass().getDeclaredFields();
                for (Field field : fields){
                    final CacheParam annotation = field.getAnnotation(CacheParam.class);
                    if (annotation == null){
                        continue;
                    }
                    field.setAccessible(true);
                    builder.append(localAnnotation.delimiter()).append(ReflectionUtils.getField(field, object));
                }
            }
        }
        return localAnnotation.prefix() + builder.toString();
    }

}