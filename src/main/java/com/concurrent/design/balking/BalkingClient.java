package com.concurrent.design.balking;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 测试 Balking 模式
 */
public class BalkingClient {
    public static void main(String[] args) throws InterruptedException {
        final BalkingData data = new BalkingData("balking.txt", "HelloWorld");

        //顾客线程
        Thread customer = new Thread(() -> data.change("AAAA"));
        customer.start();
        customer.join();

        //waiter线程
        IntStream.range(1, 4)
                .forEach(i ->
                        new Thread(() -> {
                            try {
                                data.save();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }).start()
                );
    }
}
