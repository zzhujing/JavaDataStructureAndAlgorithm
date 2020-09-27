package com.concurrent.juc.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * ThreadPoolExecutor 的工具类 里面有一些好用的工厂方法
 */
public class ExecutorsTest1 {
    public static void main(String[] args) throws InterruptedException {
//        cachedThreadPoolTest();
//        singleThreadPoolTest();
        newWorkStealingPoolTest();
    }

    private static void cachedThreadPoolTest() throws InterruptedException {
        /**
         * 1. 其中的BlockingQueue是只能缓存一个任务的SynchronousQueue ，并且MaximumThreadCount为Integer.Max_Value
         * 2. 60L会执行回收空闲 thread
         */
        final ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println(((ThreadPoolExecutor) executorService).getActiveCount());
        IntStream.rangeClosed(1, 100)
                .forEach(i -> {
                    executorService.execute(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName() + " : i -> begin worker.");
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });
        System.out.println(((ThreadPoolExecutor) executorService).getActiveCount());
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }

    private static void fixedThreadPoolTest() throws InterruptedException {
        /**
         * 1. 固定传入线程大小线程池，coreSize = maximum，所以不会回收线程
         * 2. LinkedBlockingQueue 可存入最多Integer.Maximum个任务
         */
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        System.out.println(((ThreadPoolExecutor) executorService).getActiveCount());
        IntStream.rangeClosed(1, 100)
                .forEach(i -> {
                    executorService.execute(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName() + " : i -> begin worker.");
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });
        System.out.println(((ThreadPoolExecutor) executorService).getActiveCount());
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }

    private static void singleThreadPoolTest() throws InterruptedException {
        /**
         * 1. 只有一个线程执行
         * 2. LinkedBlockingQueue 可存入最多Integer.Maximum个任务
         * 3. 相当于 newFixedThreadExecutor(1)
         * 4. 相当 new Thread() 来说。使用SingleThreadPoolExecutor能保存任务到执行队列中，并且处理完之后线程不会结束。
         */
        final ExecutorService executorService =  Executors.newSingleThreadExecutor();
        IntStream.rangeClosed(1, 100)
                .forEach(i -> {
                    executorService.execute(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName() + " : i -> begin worker.");
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }

    private static void newWorkStealingPoolTest() throws InterruptedException {
        /**
         * 使用当前所有CPU核心数来进行forkJoin异步调度,并且内部使用分治思想将任务分配到多个BlockingQueue减少资源的抢夺，并且内部会动态调整Thread大小。无法保证任务的执行顺序
         */
        final ExecutorService executorService = Executors.newWorkStealingPool();
        IntStream.rangeClosed(1, 100)
                .forEach(i -> {
                    executorService.execute(() -> {
                        try {
                            System.out.println(Thread.currentThread().getName() + " : " + i + " -> begin worker.");
                            TimeUnit.SECONDS.sleep(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
    }
}
