package com.concurrent.juc.utils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 信号量Api测试
 * 1. acquire(int times) -> 一次获取多个凭证   -> 可以通过在其他线程进行Thread#interrupt()打断获取信号量的阻塞
 * 2. availablePermits() -> 获取到现存的凭证数量
 * 3. getQueueLength() -> 获取到等待凭证的线程数量
 * 4. acquireUninterruptibly() -> 不接受打断的获取信号量。
 * 5. drainAcquired() -> 一次获取所有的凭证
 * 6. getWaitingThreads() -> 获取所有正在等待获取凭证的线程
 * 7. tryAcquired() -> 尝试获取凭证
 */
public class SemaphoreApiTest {
    public static void main(String[] args) throws InterruptedException {

        final Semaphore semaphore = new Semaphore(2);

        final Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire(2);
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "-> get Semaphore.");
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + "-> be interrupted!");
            } finally {
                semaphore.release(2);
                System.out.println(Thread.currentThread().getName() + "-> released Semaphore.");
            }
        });
        t1.start();

        TimeUnit.MILLISECONDS.sleep(50);

        final Thread t2 = new Thread(() -> {
            try {
                semaphore.acquireUninterruptibly();
                System.out.println(Thread.currentThread().getName() + "-> get Semaphore.");
            }  finally {
                semaphore.release();
                System.out.println(Thread.currentThread().getName() + "-> released Semaphore.");
            }
        });
        t2.start();

        TimeUnit.MILLISECONDS.sleep(100);
        t2.interrupt();

        for (; ; ) {
            System.out.println(semaphore.availablePermits()); //获取到现存的凭证数量
            System.out.println(semaphore.getQueueLength()); //获取到等待获取凭证的线程数
            TimeUnit.SECONDS.sleep(1);
        }

    }
}
