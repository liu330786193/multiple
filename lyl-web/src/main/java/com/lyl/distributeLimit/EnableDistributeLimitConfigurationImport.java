package com.lyl.distributeLimit;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/7 下午3:55
 */
public class EnableDistributeLimitConfigurationImport implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{RedisLimiterHelper.class.getName(), LimitInterceptor.class.getName()};
    }

}
