package com.concurrent.juc.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDownLatch 计数器使用一
 * 1. 等待所有的任务执行完毕，并行处理任务
 */
public class CountDownLatchTest {

    private static ExecutorService service = Executors.newFixedThreadPool(2);

    private static final CountDownLatch latch = new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        //1. query data from db
        int[] data = query();
        //2. execute task queue
        for (int i = 0; i < data.length; i++) {
            service.execute(new CalculateTask(data, i, latch));
        }
        //3. wait all of task down
        latch.await();
        //4. shutdown with threadPool
        service.shutdown();
        //another use ThreadPool await..
//        service.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("all of service finished!");
    }

    static class CalculateTask implements Runnable {

        private final CountDownLatch latch;
        private final int index;
        private final int[] data;

        public CalculateTask(int[] data, int i, CountDownLatch latch) {
            this.data = data;
            this.index = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            final int tmp = data[index];
            if (tmp % 2 == 0) {
                data[index] = tmp * 2;
            } else {
                data[index] = tmp * 10;
            }
            System.out.println(Thread.currentThread().getName() + " -> finished no " + index);
            latch.countDown();
        }
    }

    private static int[] query() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }
}
