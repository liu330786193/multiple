package com.lyl;

import com.lyl.core.enable.EnableLettuceConfiguration;
import com.lyl.core.lock.autoconfig.EnableDulplicateConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLettuceConfiguration
@EnableDulplicateConfiguration
@SpringBootApplication(scanBasePackages = "com.lyl")
public class LylWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LylWebApplication.class, args);
    }
}
