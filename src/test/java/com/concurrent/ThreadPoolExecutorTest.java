package com.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor tpl = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), new ThreadPoolExecutor.AbortPolicy()
        );
        for (int i = 0; i < 10; i++) {
            tpl.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("aaa");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        while (!tpl.isTerminated()) {
            System.out.println(tpl.getQueue().size());
        }
        tpl.awaitTermination(1, TimeUnit.MINUTES);
        tpl.shutdown();
        System.out.println("MainThread Finished!!");
    }
}
