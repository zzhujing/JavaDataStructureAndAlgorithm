package com.concurrent.juc.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * {@link Semaphore} 信号量，包含多个permit，获取到凭证才能获取到执行权，否则调用acquire方法则会block
 */
public class SemaphoreTest1 {

    public static void main(String[] args) {
        final SemaphoreLock lock = new SemaphoreLock();
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + "-> running");
                    lock.lock();
                    System.out.println(Thread.currentThread().getName() + "-> get semaphore lock");
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                    System.out.println(Thread.currentThread().getName() + "-> get released lock");
                }
            }).start();
        }
    }
    static class SemaphoreLock {
        private final Semaphore semaphore = new Semaphore(1);

        public void lock() throws InterruptedException {
            semaphore.acquire();
        }

        public void unlock() {
            semaphore.release();
        }
    }
}
