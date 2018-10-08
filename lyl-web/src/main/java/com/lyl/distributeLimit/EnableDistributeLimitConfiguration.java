package com.lyl.distributeLimit;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/7 下午3:54
 */

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({EnableDistributeLimitConfigurationImport.class})
public @interface EnableDistributeLimitConfiguration {
}
