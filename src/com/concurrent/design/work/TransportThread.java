package com.concurrent.design.work;


import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 传输线程，负责将任务放置到任务队列中
 */
public class TransportThread extends Thread {

    private final Channel channel;
    private static final Random random = new Random(System.currentTimeMillis());
    private static final AtomicInteger i = new AtomicInteger(0);
    public TransportThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        for (; ; ) {
            Request request = new Request(String.valueOf(i.getAndIncrement()));
            channel.put(request);
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
