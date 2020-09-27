package com.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * {@link java.util.concurrent.locks.Condition} 实例
 **/
public class ConditionDemo {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static AtomicInteger ai = new AtomicInteger();

    public static void main(String[] args) {

        IntStream.rangeClosed(0,2)
                .forEach(i -> new Thread(() -> {
                    try {
                        while (true) {
                            lock.lock();
                            while (ai.get() % 3 != i) {
                                condition.await();
                            }
                            System.out.println(i);
                            ai.getAndIncrement();
                            condition.signalAll();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }).start());
    }
}
