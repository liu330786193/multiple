package com.lyl.demo.aliyun.exception;

/**
 * @Author lyl
 * @Description
 * @Date 2018/9/30 下午2:36
 */
public class CustomException extends RuntimeException {

    private int code;

    public CustomException() {
        super();
    }

    public CustomException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


}
