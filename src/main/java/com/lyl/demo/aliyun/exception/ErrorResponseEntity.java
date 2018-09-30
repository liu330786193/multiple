package com.lyl.demo.aliyun.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 下午2:38
 */
@Data
@AllArgsConstructor
public class ErrorResponseEntity {

    private int code;
    private String message;

}
