package com.concurrent.juc.utils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition测试类 -> 模拟生产者消费者模式
 */
public class ConditionTest1 {

    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition condition = lock.newCondition();
    private static int data = 0; //share data
    private static volatile boolean noUse = true;

    /**
     * 1. 不使用condition行吗？ 不行，在非公平锁状态下无法做到生产后提醒消费者消费
     * 2. 不用lock行吗？ 不行，会直接报异常。类似Object的wait()/notify()等方法没有加synchronized
     * 3. condition.await() block住了，其他线程如何获取到锁？ 和wait()等一样会放弃cpu执行权
     *
     * @param args
     */
    public static void main(String[] args) {
        ConditionTest1 test = new ConditionTest1();
        new Thread(() -> {
            for (; ; ) {
                test.createData();
            }
        }).start();
        new Thread(() -> {
            for (; ; ) {
                test.useData();
            }
        }).start();
    }


    private void createData() {
        try {
            lock.lock();
            while (noUse) {
                condition.await();
            }
            //product data
            System.out.println("P ->" + ++data);
            TimeUnit.SECONDS.sleep(1);
            noUse = true;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private void useData() {
        try {
            lock.lock();
            while (!noUse) {
                condition.await();
            }
            //product data
            System.out.println("C ->" + data);
            noUse = false;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
