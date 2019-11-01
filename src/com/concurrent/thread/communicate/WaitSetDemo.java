package com.concurrent.thread.communicate;

import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/10/31
 * WaitSet的特点：
 * 1. 每一个对象都有自己的WaitSet用来存放调用wait()方法之后的阻塞状态
 * 2. 被唤醒之后要重新获取锁
 * 3. 被唤醒的顺序不一定是FIFO
 * 4. 被唤醒后不一定立即执行
 */
public class WaitSetDemo {

    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        IntStream.rangeClosed(0, 9)
                .forEach(i -> new Thread(() -> {
                    synchronized (LOCK) {
                        System.out.println(i + " -> will coming to wait set.");
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(i + " -> will leave to wait set.");
                    }
                }).start());

        Thread.sleep(2000);

        IntStream.rangeClosed(0, 9)
                .forEach(i -> {
                    synchronized (LOCK) {
                        LOCK.notify();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
