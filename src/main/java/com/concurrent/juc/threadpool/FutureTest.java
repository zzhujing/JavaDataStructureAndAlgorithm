package com.concurrent.juc.threadpool;

import java.util.concurrent.*;

/**
 * 测试future相关API
 * 1. get() 打断的是谁 -> caller调用future.get()
 * 2. get()方法timeout之后原来的Task还会执行吗？ 会
 * 3. cancel(boolean mayInterruptIfRunning)方法会根据传递的Boolean值来判断是否在Task运行的时候进行打断。
 * 4. cancel之后的Task还是会继续运行。
 * 5. cancel之后是无法通过get()获取到结果的。
 */
public class FutureTest {
    public static void main(String[] args) throws InterruptedException {
        testGetTimeout();
//        testRunningIfCancel();
    }

    private static void testGetTimeout() {
        final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        final Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("do some task ...");
            return "finished!";
        });
        String result = null;
        try {
            //future#get(long timeout) 超时之后原来的Task还是会运行到结束为止。可以通过直接结束线程池
            result = future.get(2, TimeUnit.SECONDS);
            System.out.println(result);
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            e.printStackTrace();
            //取消任务并且关闭线程池。
            System.out.println(future.cancel(true));
            System.out.println(executorService.getActiveCount());
            executorService.shutdown();
        }
    }

    private static void testRunningIfCancel() throws InterruptedException {
        final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        final Future<String> future = executorService.submit(() -> {
            TimeUnit.SECONDS.sleep(10);
            System.out.println("do some task ...");
            return "finished!";
        });
        TimeUnit.SECONDS.sleep(1);
        //cancel会去尝试中断，然后继续执行任务
        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());
    }
}
