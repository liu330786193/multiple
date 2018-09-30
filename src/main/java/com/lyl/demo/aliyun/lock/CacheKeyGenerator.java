package com.lyl.demo.aliyun.lock;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 上午10:20
 */
public interface CacheKeyGenerator {


    /**
     * 获取AOP参数，生成制定缓存key
     */
    String getLockKey(ProceedingJoinPoint pjp);

}
