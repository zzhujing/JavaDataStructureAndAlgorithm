package com.concurrent.design.threadpremessage;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class Message {

    private final String value;

    public Message(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
