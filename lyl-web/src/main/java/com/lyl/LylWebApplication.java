package com.lyl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.lyl")
public class LylWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(LylWebApplication.class, args);
    }
}
