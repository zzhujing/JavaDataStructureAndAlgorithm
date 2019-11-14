package com.concurrent.juc.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

public class AtomicIntegerTest {
    private static volatile int no = 0;
    public static final int MAX = 500;

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);

        IntStream.rangeClosed(1, 3)
                .forEach(i -> new Thread(() -> {
                    while (no < MAX) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + " -> " + no++);
                    }
                    latch.countDown();
                }, String.valueOf(i)).start());

        latch.await();
        System.out.println("=====finished======");
    }
}
