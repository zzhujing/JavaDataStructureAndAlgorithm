package com.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadLocalDemo {

    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 5).boxed()
                .forEach(i -> new Thread(() -> {
                    try {
                        setThreadName();
                        System.out.println(getThreadName());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }, String.valueOf(i)).start());
    }


    public static String getThreadName() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Main->" + Thread.currentThread().getName());
        return Thread.currentThread().getName() + " : from threadLocal ->" + threadLocal.get();
    }

    public static void setThreadName() {
        threadLocal.set(Thread.currentThread().getName());
    }


}
