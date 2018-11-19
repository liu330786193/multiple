package com.lyl;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.net.URLDecoder;

@Configuration
public class JarConfig implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        String jarFilePath = JarConfig.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        jarFilePath = URLDecoder.decode(jarFilePath, "UTF-8");
        System.out.println(jarFilePath);
        FileInputStream fileInputStream = new FileInputStream("file:/C:/Users/lyl/Desktop/conf.jar!/1.txt");
    }
}
