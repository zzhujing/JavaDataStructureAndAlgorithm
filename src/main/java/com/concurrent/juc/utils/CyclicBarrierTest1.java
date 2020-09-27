package com.concurrent.juc.utils;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * 循环屏障
 * <p>
 * 在一个或者多个线程中等待到一个共同节点同时释放。相对于{@link java.util.concurrent.CountDownLatch}
 * 1. 能自动回调注入的任务，且能重置次数reset()
 * 2. 工作线程之间相互协调，而CountDownLatch则不关心。
 */
public class CyclicBarrierTest1 {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        //传递进行的Runnable为回调方法
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println(Thread.currentThread().getName() + "-> finished!"));
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " -> begin work");
            try {
                Thread.sleep(10000);
                System.out.println(Thread.currentThread().getName() + "-> finished!");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + " -> CyclicBarrier finished!");
                //                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(5000);
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }).start();

        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(cyclicBarrier.getNumberWaiting());//1
        System.out.println(cyclicBarrier.getParties()); //2
        System.out.println(cyclicBarrier.isBroken()); //false
        //reset
        cyclicBarrier.reset();

        System.out.println(cyclicBarrier.getNumberWaiting());//0
        System.out.println(cyclicBarrier.getParties()); //2
        System.out.println(cyclicBarrier.isBroken()); //false
    }
}
