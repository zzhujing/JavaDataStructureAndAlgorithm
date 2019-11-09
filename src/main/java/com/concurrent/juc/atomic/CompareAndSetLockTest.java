package com.concurrent.juc.atomic;

import java.util.stream.IntStream;

public class CompareAndSetLockTest {

    public static final CompareAndSetLock lock = new CompareAndSetLock();

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 3)
                .forEach(i -> new Thread(() -> {
                    try {
                        doSomething();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (GetLockException e) {
                        e.printStackTrace();
                    }
                }, String.valueOf(i)).start());
    }

    public static void doSomething() throws InterruptedException, GetLockException {
        try {
            lock.lock();
            System.out.println("doSomething....");
            Thread.sleep(100000L);
        } finally {
            lock.unlock();
        }
    }
}
