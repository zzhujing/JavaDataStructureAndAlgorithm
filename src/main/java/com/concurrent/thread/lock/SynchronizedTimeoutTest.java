package com.concurrent.thread.lock;

import com.concurrent.thread.lock.BooleanLock;
import com.concurrent.thread.lock.Lock;

import java.util.stream.Stream;

/**
 * @author : hujing
 * @date : 2019/10/29
 * 使用自定义锁来添加超时功能
 */
public class SynchronizedTimeoutTest {
    private static final BooleanLock lock = new BooleanLock();

    public static void main(String[] args) throws InterruptedException {
//        Thread t = new Thread(SynchronizedTimeoutTest::m1);
//        t.start();
//        Thread.sleep(200L);
//        //尝试打断,可以打断但是不会释放锁
//        t.interrupt();
//        System.out.println(t.isInterrupted());
//        new Thread(SynchronizedTimeoutTest::m1).start();


        Stream.of("t1", "t2").forEach(name -> {
            new Thread(() -> {
                try {
                    //带超时时间的获取lock方法，10ms没有获取到锁则抛出TimeoutException
                    lock.lock(1000L);
                    m1();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Lock.TimeOutException e) {
                    //这里处理超时之后的逻辑
                    System.out.println(e.getMessage());
                } finally {
                    try {
                        //释放锁
                        lock.unlock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, name).start();
        });

    }

    public static synchronized void m1() {
        System.out.println("m1 invoked!");
        //模拟复杂逻辑执行时间
        while (true) {
        }
    }
}
