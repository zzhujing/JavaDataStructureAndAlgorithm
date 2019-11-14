package com.concurrent.design.countdown;

import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class CountDownClient {
    public static void main(String[] args) throws InterruptedException {

        final CustomCountDown countDown = new CustomCountDown(5);

        IntStream.rangeClosed(1, 5)
                .forEach(i -> new Thread(() -> {
                    System.out.println(Thread.currentThread().getName() + " is working ..");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countDown.down();
                }, String.valueOf(i)).start());
        countDown.await();

        System.out.println("FINISHED");
    }
}
