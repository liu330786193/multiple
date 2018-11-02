package com.lyl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/29 上午11:54
 */
@Configuration
public class StaticTest implements InitializingBean {

    public static String NAME;

    @Value("${static.name}")
    private String name;

    @Override
    public void afterPropertiesSet() throws Exception {
        NAME = name;
    }
}
