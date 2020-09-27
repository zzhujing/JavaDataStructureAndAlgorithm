package com.concurrent.juc.utils;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

/**
 * 使用{@link java.util.concurrent.CyclicBarrier} 进行多线程的多次任务协调等待
 **/
public class CyclicBarrierTest2 {

    private static final CyclicBarrier cb = new CyclicBarrier(4);

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        IntStream.rangeClosed(1, 3)
                .forEach(i -> new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "执行完任务A啦，此时执行完任务的个数为:" + cb.getNumberWaiting());
                        cb.await();
/*                        Thread.sleep(2000);
                        System.out.println(Thread.currentThread().getName() + "执行完任务B啦，此时执行完任务的个数为:" + cb.getNumberWaiting());
                        cb.await();
                        Thread.sleep(500);
                        System.out.println(Thread.currentThread().getName() + "执行完任务C啦，此时执行完任务的个数为:" + cb.getNumberWaiting());
                        cb.await();*/
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }

                }, i + "").start());
        cb.await();
        System.out.println("Main Thread over");
    }
}
