package com.concurrent.juc.utils;

import java.util.concurrent.CountDownLatch;

/**
 * 可以在一个或者多个线程中等待另外一个线程完成自己的任务
 * 几种结束await()方法的方式
 * 1. countDown()到为0
 * 2. 外部线程进行打断
 * 3. 超时时间配置
 */
public class CountDownLatchTest2 {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread t1 = new Thread(() -> {
            try {
                latch.await();
                System.out.println("await finished!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread tmp = new Thread(() -> {
            try {
                latch.await();
                System.out.println("start work");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final Thread t2 = new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    t1.interrupt();
                    tmp.interrupt();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
        t1.start();
        t2.start();
        tmp.start();
        t1.join();
        t2.join();
        tmp.join();
        System.out.println("FINISHED");

    }
}
