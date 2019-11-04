package com.concurrent.design.prodandcons;

import java.util.Random;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ConsumerThread extends Thread {
    private final MessageQueue queue;
    private final Random random;
    private final int no;

    public ConsumerThread(MessageQueue queue,int no) {
        super("CONSUMER ： " + no);
        this.no = no;
        this.queue = queue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (true) {
            Message message = queue.get();
            System.out.println(Thread.currentThread().getName() + " consume the message -> " + message.getMessage());
        }
    }
}
