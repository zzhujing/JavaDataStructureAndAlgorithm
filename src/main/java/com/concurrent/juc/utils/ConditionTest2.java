package com.concurrent.juc.utils;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * Condition实现生产者消费者模式
 */
public class ConditionTest2 {
    //lock
    private static final ReentrantLock LOCK = new ReentrantLock();

    //produce and consume condition
    private static final Condition PRODUCE_COND = LOCK.newCondition();
    private static final Condition CONSUME_COND = LOCK.newCondition();
    private static final int MAX_CAPACITY = 100;
    private static final LinkedList<Long> TIMESTAMP_POOL = new LinkedList<>();


    public static void main(String[] args) {
        IntStream.rangeClosed(0, 15).forEach(i -> new Thread(() -> {
            for (; ; ) {
                product();
                sleep(1000);
            }
        }, "P-" + i).start());
        IntStream.rangeClosed(0, 5).forEach(i -> new Thread(() -> {
            for (; ; ) {
                consume();
            }
        }, "C" + i).start());

    }
    public static void sleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void product() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.size() >= MAX_CAPACITY) {
                PRODUCE_COND.await();
            }
            System.out.println("wait queue length -> " + LOCK.getWaitQueueLength(PRODUCE_COND));
            final long value = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + "-> product -> " + value);
            TIMESTAMP_POOL.addLast(value);
            CONSUME_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

    public static void consume() {
        try {
            LOCK.lock();
            while (TIMESTAMP_POOL.isEmpty()) {
                CONSUME_COND.await();
            }
            System.out.println(Thread.currentThread().getName() + "-> consume -> " + TIMESTAMP_POOL.removeFirst());
            PRODUCE_COND.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
    }

}

