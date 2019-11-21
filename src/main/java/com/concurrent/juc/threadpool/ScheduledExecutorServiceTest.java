package com.concurrent.juc.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 定时线程池
 * 1. 固定时间后执行一次
 * 2. 固定时间后执行一次然后按周期(方法执行时间会计算，并且方法时间大于周期的话会立即执行)执行
 * 3. 固定时间后执行一次然后按周期(方法执行时间不算)执行
 */
public class ScheduledExecutorServiceTest {

    private static final ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(2);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        testScheduled();
//        testScheduleWithCallable();
//        testScheduleAtFixedRate();
        testScheduleWithFixedDelay();
    }

    /**
     * 如果任务执行大于2s，那么下次任务将立即执行
     * 否则则在上次任务开始之后的2s后开始第二次执行
     */
    private static void testScheduleAtFixedRate() throws InterruptedException {
        final AtomicLong al = new AtomicLong(0L);
        System.out.println(scheduledExecutorService.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        //该参数默认为false,表示线程池shutdown之后不会继续执行异步任务
        scheduledExecutorService.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                long currentMillis = System.currentTimeMillis();
                if (al.get() == 0) {
                    System.out.println(currentMillis);
                } else {
                    System.out.println(currentMillis - al.get());
                }
                al.set(currentMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(2);
        scheduledExecutorService.shutdown();
    }

    /**
     * 在每次任务执行的两秒后进行第二次任务调度
     */
    private static void testScheduleWithFixedDelay() throws InterruptedException {
        final AtomicLong al = new AtomicLong(0L);
        scheduledExecutorService.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                long currentMillis = System.currentTimeMillis();
                if (al.get() == 0) {
                    System.out.println(currentMillis);
                } else {
                    System.out.println(currentMillis - al.get());
                }
                al.set(currentMillis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(9);
        scheduledExecutorService.shutdown();
    }

    private static void testScheduleWithCallable() throws InterruptedException, ExecutionException {
        final ScheduledFuture<Integer> future = scheduledExecutorService.schedule(() -> 2, 2, TimeUnit.SECONDS);
        TimeUnit.MILLISECONDS.sleep(500);
        future.cancel(true);
        System.out.println(future.get());
    }

    private static void testScheduled() throws InterruptedException {
        //该属性设置了之后针对schedule方法在运行的时候被shutdown了。那么依旧会继续执行完毕。
        scheduledExecutorService.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
        scheduledExecutorService.schedule(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("i will be invoked! -> " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }, 2, TimeUnit.SECONDS);
        TimeUnit.MILLISECONDS.sleep(500);
        scheduledExecutorService.shutdown();
    }
}
