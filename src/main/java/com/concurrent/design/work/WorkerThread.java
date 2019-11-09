package com.concurrent.design.work;

import java.util.Random;

/**
 * 工作线程
 * @author : hujing
 * @date : 2019/11/4
 */
public class WorkerThread extends Thread {

    private final Channel channel;
    private static final Random random = new Random(System.currentTimeMillis());

    public WorkerThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            Request request = channel.task();
            System.out.println(Thread.currentThread().getName() + " worker -> " + request.getValue());
            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
