package com.lyl;

import com.lyl.core.enable.EnableLettuceConfiguration;
import com.lyl.core.lock.autoconfig.EnableDulplicateConfiguration;
import com.lyl.id.LuaScriptServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@EnableLettuceConfiguration
@EnableDulplicateConfiguration
@SpringBootApplication(scanBasePackages = "com.lyl")
public class LylWebApplication {

    @Resource
    private LuaScriptServiceImpl luaScriptService;

    public static void main(String[] args) {
        SpringApplication.run(LylWebApplication.class, args);
    }


    @GetMapping("test/lua")
    public void testLua(){
        luaScriptService.redisAddScriptExec();
    }

}
