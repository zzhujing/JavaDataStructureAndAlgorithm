package com.concurrent.juc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

/**
 * 读写锁测试
 * R W -> N
 * R R -> Y
 * W R -> N
 * W W -> N
 */
public class RWLockTest {

    private final ReadWriteLock rdLock = new ReentrantReadWriteLock();
    private final List<Long> data = new ArrayList<>();
    private final Lock readLock = rdLock.readLock();
    private final Lock writeLock = rdLock.writeLock();

    public static void main(String[] args) {
        RWLockTest test = new RWLockTest();
        IntStream.rangeClosed(1, 2)
                .forEach(i -> new Thread(test::write).start());
    }
    private void read() {
        try {
            readLock.lock();
            System.out.println("begin read");
            data.forEach(System.out::println);
        } finally {
            readLock.unlock();
        }
    }

    private void write() {
        try {
            writeLock.lock();
            System.out.println("begin write");
            data.add(System.currentTimeMillis());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            writeLock.unlock();
        }

    }

}
