package com.concurrent.juc.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 使用CountDownLatch管理多个任务的批量完成回调
 */
public class CountDownLatchTest3 {
    public static void main(String[] args) throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);
        Watcher watcher = new Watcher(latch);
        executorService.execute(new TaskRunnable1(latch, watcher));
        executorService.execute(new TaskRunnable2(latch, watcher));
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("Main Thread Exit!!");
    }


    static class TaskRunnable1 implements Runnable {

        private final CountDownLatch latch;
        private final Watcher watcher;

        TaskRunnable1(CountDownLatch latch, Watcher watcher) {
            this.latch = latch;
            this.watcher = watcher;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " -> taskRunnable 1 ->finished!");
                    watcher.watch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class TaskRunnable2 implements Runnable {

        private final CountDownLatch latch;
        private final Watcher watcher;

        TaskRunnable2(CountDownLatch latch, Watcher watcher) {
            this.latch = latch;
            this.watcher = watcher;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + " -> taskRunnable 2 ->finished!");
                    watcher.watch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Watcher {
        private CountDownLatch latch;

        public Watcher(CountDownLatch latch) {
            this.latch = latch;
        }

        public void watch() {
            latch.countDown();
            if (latch.getCount() == 0) {
                System.out.println("update db..");
            }
        }
    }
}
