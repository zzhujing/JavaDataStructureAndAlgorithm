package com.concurrent.juc.utils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁测试
 * 1. lock 不可被打断
 * 2. unlock
 * 3. tryLock 可被打断
 * 4. lockInterruptibly 可被打断
 *
 * Monitor 功能方法
 * 1. {@link ReentrantLock#getHoldCount()} 获取当前线程持有的锁数量
 * 2. {@link ReentrantLock#getOwner()} 获取当前锁持有线程
 * 3. {@link ReentrantLock#getWaitingThreads(Condition)} 获取等待的线程集合
 * 4. {@link ReentrantLock#getQueueLength()} 获取等待的线程数
 */
public class ReentrantLockTest {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        final Thread t1 = new Thread(ReentrantLockTest::testLock);
        t1.start();
        TimeUnit.MILLISECONDS.sleep(50);
        final Thread t2 = new Thread(ReentrantLockTest::testLock);
        t2.start();
        TimeUnit.MILLISECONDS.sleep(50);
        t2.interrupt();

        System.out.println(lock.getQueueLength());
        //当前是否有线程在等待
        System.out.println(lock.hasQueuedThreads());
        //是否有线程获取到锁
        System.out.println(lock.isLocked());
        //是否是当前线程持有的锁
        System.out.println(lock.isHeldByCurrentThread());
    }

    private static void testLock() {
        try {
            //可被中断
            if (lock.tryLock(10, TimeUnit.SECONDS)) {
                try {
                    //获取当前线程持有该锁的次数
                    Optional.of(Thread.currentThread().getName() + " -> " + lock.getHoldCount()).ifPresent(System.out::println);
                    while (true) {

                    }
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
