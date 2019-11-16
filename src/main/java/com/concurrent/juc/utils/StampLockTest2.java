package com.concurrent.juc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * java8Stamp锁的使用
 * <p>
 * 当上面使用过的{@link com.concurrent.design.readwriter.ReadWriteLock} 其中的读写线程分配差距很大的时候有可能有一方会很难抢到锁。这时候需要另一方进行乐观锁判断
 */
public class StampLockTest2 {

    public static final StampedLock stampedLock = new StampedLock();
    private static final List<Long> data = new ArrayList<>();

    public static void main(String[] args) {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable read = () -> {
            for (; ; ) {
                read();
            }
        };
        Runnable write = () -> {
            for (; ; ) {
                write();
            }
        };
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(read);
        executorService.submit(write);
    }
    public static void read() {
        //当读写差距很大的时候需要尝试获取乐观读锁
        long stamp = stampedLock.tryOptimisticRead();
        //校验是否有线程进行写操作
        if (stampedLock.validate(stamp)) {
            try {
                stamp = stampedLock.readLock();
                System.out.println(Thread.currentThread().getName() + " -> read : " + data.stream().map(String::valueOf).collect(Collectors.joining(",", "R_", "")));
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }

    public static void write() {
        long stamped = -1;
        try {
            stamped = stampedLock.writeLock();
            data.add(System.currentTimeMillis());
            sleep(100);
        } finally {
            stampedLock.unlockWrite(stamped);
        }
    }

    public static void sleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
