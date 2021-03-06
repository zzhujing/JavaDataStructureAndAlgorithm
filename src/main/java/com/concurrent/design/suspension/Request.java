package com.concurrent.design.suspension;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class Request {

    private final String value;

    public Request(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Request{" +
                "value='" + value + '\'' +
                '}';
    }
}
