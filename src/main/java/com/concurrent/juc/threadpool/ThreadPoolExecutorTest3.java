package com.concurrent.juc.threadpool;


import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 测试线程池中一些属性和方法
 * 1. 通过设置线程池属性{@link java.util.concurrent.ThreadPoolExecutor#allowCoreThreadTimeOut(boolean)}。允许回收coreSizeThread..
 * 2. 可以remove当前线程池中任务队列的任务
 * 3. 可以在不提交任务的时候激活工作线程
 * 4. ThreadPoolExecutorAdvice增强。
 */
public class ThreadPoolExecutorTest3 {
    public static void main(String[] args) throws InterruptedException {
//        testAllowCoreThreadTimeOut();
//        testRemoveCurrentTaskInQueue();
        testMyThreadPoolExecutor();
    }

    private static void testAllowCoreThreadTimeOut() {
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        //在不提交Task的时候激活线程。
        executor.prestartCoreThread();
        //设置回收时间
        executor.setKeepAliveTime(5, TimeUnit.SECONDS);
        //允许回收coreThread
        executor.allowCoreThreadTimeOut(true);
        System.out.println(executor.getCorePoolSize());
    }

    private static void testRemoveCurrentTaskInQueue() throws InterruptedException {
        final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println("i'm finished.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.MILLISECONDS.sleep(20);
        executor.remove(executor.getQueue().peek());
        TimeUnit.MILLISECONDS.sleep(20);
        executor.execute(new MyRunnable());
    }

    private static void testMyThreadPoolExecutor() {
        final MyThreadPoolExecutor threadPoolExecutor = new MyThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1), Thread::new, new ThreadPoolExecutor.AbortPolicy());

        IntStream.range(0, 2)
                .forEach(i -> threadPoolExecutor.execute(() -> System.out.println("----------")));
        threadPoolExecutor.shutdown();
    }


    private static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("========just do me ==========");
        }
    }

    private static class MyThreadPoolExecutor extends ThreadPoolExecutor {

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("before..");
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println("after..");
        }
    }
}
