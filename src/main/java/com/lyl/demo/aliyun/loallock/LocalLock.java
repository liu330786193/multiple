package com.lyl.demo.aliyun.loallock;

import java.lang.annotation.*;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 下午4:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LocalLock {

    /**
     * @author lyl
     */
    String key() default "";

    /**
     * 过期时间 TODO 由于用的 guava 暂时就忽略这属性吧 集成 redis 需要用到
     *
     * @author lyl
     */
    int expire() default 5;

}
