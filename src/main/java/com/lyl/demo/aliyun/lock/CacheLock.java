package com.lyl.demo.aliyun.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 上午10:15
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CacheLock {


    /**
     * redis前缀
     */
    String prefix() default "";

    /**
     * 过期秒数
     */
    int expire() default 5;

    /**
     * 超时时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * <p>
     *     key的分隔符
     * </p>
     */
    String delimiter() default ":";
}
