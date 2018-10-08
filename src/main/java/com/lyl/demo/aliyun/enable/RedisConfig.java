package com.lyl.demo.aliyun.enable;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @Author lyl
 * @Description
 * @Date 2018/10/7 下午2:45
 */
@Data
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private RedisCommands<String, String> redisCommands;

    @Bean
    public RedisClient redisClient(){
        return RedisClient.create(RedisURI.create("redis://" + host + ":" + port));
    }

}