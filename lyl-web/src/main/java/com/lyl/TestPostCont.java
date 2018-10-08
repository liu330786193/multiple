package com.lyl;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/21 下午3:29
 */
@Service
public class TestPostCont {

    private int id;
    private static int age;

    @PostConstruct()
    public void init(){

        this.id = 10;

    }

    static {
        age = 10;
    }

    public int getId() {
        return id;
    }

    public static int getAge() {
        return age;
    }

    public static void setAge(int age) {
        TestPostCont.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }
}
