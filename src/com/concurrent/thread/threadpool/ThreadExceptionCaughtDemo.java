package com.concurrent.thread.threadpool;

import java.util.concurrent.*;

/**
 * @author : hujing
 * @date : 2019/11/13
 * 多线程异常捕获
 * <p>
 * 1. 对于线程池的{@link ExecutorService#execute(Runnable)}方法来说，是需要再创建线程池的时候传递自己的线程工厂类并设置好线程异常捕获执行器
 * 2. 对于{@link ExecutorService#submit(Runnable)}方法,则可以使用{@link Future#get()} 来捕获异常 -> 喜欢这种
 * 3. 直接在子线程中进行try..catch..
 */
public class ThreadExceptionCaughtDemo {
    private static final ExecutorService service = Executors.newFixedThreadPool(
            5,
            new MyThreadFactory()
    );

    public static void main(String[] args) throws InterruptedException {
        final Future<?> f1 = service.submit((Callable<? extends Object>) () -> {
            throw new RuntimeException("throws error");
        });
        final Future<?> f2 = service.submit(() -> System.out.println("execute success!"));
        service.shutdown();
        service.awaitTermination(1, TimeUnit.MINUTES);
        try {
            f1.get();
            f2.get();
        } catch (ExecutionException e) {
            System.out.println("caught thread exception..");
            e.printStackTrace();
        }

    }


    private static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            return t;
        }
    }

    private static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println(t.getName() + " , " + e.getMessage());
            //可以在这里进行全局事务回滚
        }
    }
}
