package com.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class VolatileTest {

    private static AtomicInteger init = new AtomicInteger(1);

    public static void main(String[] args) {

        IntStream.rangeClosed(0, 5).boxed()
                .forEach(i -> new Thread(() -> {
                    int per;
                    while ((per = init.getAndIncrement()) < 50) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                            System.out.println(per);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start());
    }
}
