package com.concurrent.juc.utils;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * ForkJoinPool# RecursiveTask使用带返回值的分而治之求和
 */
public class ForkJoinRecursiveTest {

    private static final int MAX_THRESHOLD = 5;

    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final ForkJoinTask<Integer> future = forkJoinPool.submit(new CalculateRecursiveTask(0, 1000));
        try {
            final Integer result = future.get();
            System.out.println(result);
            forkJoinPool.shutdown();
            forkJoinPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //Runnable执行方法报错
            e.printStackTrace();
        }
    }

    /**
     * 计算任务
     */
    private static class CalculateRecursiveTask extends RecursiveTask<Integer> {

        private final int begin;
        private final int end;


        private CalculateRecursiveTask(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - begin <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(begin, end).sum();
            } else {
                //fork and join
                int mid = (end + begin) / 2;
                CalculateRecursiveTask leftTask = new CalculateRecursiveTask(begin, mid);
                CalculateRecursiveTask rightTask = new CalculateRecursiveTask(mid + 1, end);
                leftTask.fork();
                rightTask.fork();
                return leftTask.join() + rightTask.join();
            }
        }
    }
}
