package com.concurrent.juc.utils;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.LongStream;

/**
 * ForkJoin 没有返回值的任务测试类
 */
public class ForkJoinRecursiveActionTest {

    //使用全局变量来获取返回结果
    private static final AtomicLong sum = new AtomicLong(0);

    public static void main(String[] args) throws InterruptedException {
        final ForkJoinPool pool = new ForkJoinPool();
        pool.execute(new CalculateRecursiveAction(1,10000));
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
        Optional.of(sum).ifPresent(System.out::println);
    }


    private static class CalculateRecursiveAction extends RecursiveAction {

        private final long begin;
        private final long end;
        public static final long MAX_THRESHOLD = 20;

        private CalculateRecursiveAction(long begin, long end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - begin <= MAX_THRESHOLD) {
                LongStream.rangeClosed(begin, end).forEach(sum::getAndAdd);
            } else {
                long mid = (end + begin) / 2;
                CalculateRecursiveAction left = new CalculateRecursiveAction(begin, mid);
                CalculateRecursiveAction right = new CalculateRecursiveAction(mid + 1, end);
                left.fork();
                right.fork();
            }
        }
    }
}
