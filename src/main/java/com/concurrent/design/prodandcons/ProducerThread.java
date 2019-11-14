package com.concurrent.design.prodandcons;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ProducerThread extends Thread {

    private final MessageQueue queue;
    private final Random random;
    private final int no;
    private final AtomicInteger count = new AtomicInteger(0);

    public ProducerThread(MessageQueue queue, int no) {
        super("PRODUCER : " + no);
        this.no = no;
        this.queue = queue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (true) {
            Message message = new Message("producer -> " + count.getAndIncrement());
            queue.put(message);
            System.out.println(Thread.currentThread().getName() + " product the message -> " + message.getMessage());
        }
    }
}
