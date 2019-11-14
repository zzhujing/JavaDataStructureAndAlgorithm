package com.concurrent.design.countdown;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 使用原生JDK的{@link CountDownLatch} 实现等待
 */
public class JDkCountDownLatchClient {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(5);

        IntStream.rangeClosed(1, 5)
                .forEach(i -> new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + " is working ..");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    latch.countDown();
                }, String.valueOf(i)).start());
        latch.await();
        System.out.println("FINISHED");
    }
}
