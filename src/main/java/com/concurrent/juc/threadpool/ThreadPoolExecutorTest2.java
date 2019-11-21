package com.concurrent.juc.threadpool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 线程池之关闭
 * shutdown() -> 非阻塞，会将空闲线程interrupt()然后继续执行没执行完的Task。
 * shutdownNow() -> 非阻塞，会打断所有线程。并将BlockingQueue中为处理的任务返回
 * 当Task任务十分耗时的时候可以设置为守护线程从外面结束其生命周期
 */
public class ThreadPoolExecutorTest2 {
    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new MyThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy() //拒绝策略
        );

        IntStream.rangeClosed(1, 20)
                .boxed()
                .forEach(i -> threadPoolExecutor.execute(() -> {
                    //若Task中执行一些费时间的任务，使用shutdown或者shutdownNow是无法中断的。此时需要将其设置为守护线程。使用Main函数将其结束
                    for (; ; ) {
                    }
                }));

        final List<Runnable> runnableList = threadPoolExecutor.shutdownNow();
        System.out.println(runnableList);
        System.out.println(runnableList.size());
        threadPoolExecutor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("main thread done.");
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
            final Thread t1 = new Thread(r);
            t1.setDaemon(true);
            return t1;
        }
    }
}
