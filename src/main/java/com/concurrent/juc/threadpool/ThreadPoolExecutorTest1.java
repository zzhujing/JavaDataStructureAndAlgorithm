package com.concurrent.juc.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池之七大参数
 */
public class ThreadPoolExecutorTest1 {
    public static void main(String[] args) {
        /**
         * 下面七个主要的线程池参数
         * int corePoolSize,  核心线程数量
         * int maximumPoolSize, 最大worker线程数量
         * long keepAliveTime,  超出核心线程的线程空闲时间
         * TimeUnit unit,   时间单位
         * BlockingQueue<Runnable> workQueue, 任务队列，里面使用显示锁+Condition实现了阻塞队列
         * ThreadFactory threadFactory, 线程工厂
         * RejectedExecutionHandler handler 拒绝策略
         */
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                new MyThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() //拒绝策略
        );
        threadPoolExecutor.submit(() -> sleep(100));
        threadPoolExecutor.submit(() -> sleep(10));
        threadPoolExecutor.submit(() -> sleep(10));
        int activeCount = -1;
        int queueSize = -1;
        for (; ; ) {
            final int ac = threadPoolExecutor.getActiveCount();
            final int qs = threadPoolExecutor.getQueue().size();
            if (ac != activeCount || qs != queueSize) {
                System.out.println("CoreSize -> " + threadPoolExecutor.getCorePoolSize());
                System.out.println("MaximumSize ->" + threadPoolExecutor.getMaximumPoolSize());
                System.out.println("ActiveCount -> " + ac);
                System.out.println("queueSize -> " + qs);
                System.out.println("========================");
                activeCount = ac;
                queueSize = qs;
            }
        }
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    }
}
