package com.concurrent.design.threadpremessage;

import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 模拟一个消息一个线程 , 可以使用线程池进行优化
 */
public class ThreadPreMessageClient {
    public static void main(String[] args) {
        MessageHandler messageHandler = new MessageHandler();
        Message message = new Message("ZhangSan");
        IntStream.rangeClosed(1, 10)
                .forEach(i -> messageHandler.handler(message));
        messageHandler.shutdown();
    }
}
