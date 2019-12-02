package com.concurrent.juc.concurrencycontainer;

import com.google.common.collect.Lists;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentPerformanceListTest {

    private static final List<Summary> result = Lists.newArrayList();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 10; i < 100; ) {
            performance(new ConcurrentLinkedQueue<>(), i);
            performance(new CopyOnWriteArrayList<>(), i);
            performance(Collections.synchronizedList(new ArrayList<>()), i);
            i += 10;
        }
    }

    static class Summary {
        private final int thread;
        private final long spend;

        Summary(int thread, long spend) {
            this.thread = thread;
            this.spend = spend;
        }

        @Override
        public String toString() {
            return "Summary{" +
                    "thread=" + thread +
                    ", spend=" + spend +
                    '}';
        }

        public void record(List<Summary> result) {
            result.add(this);
        }
    }

    public static void performance(Collection<Integer> list, int threshold) throws InterruptedException {
        long total = 0;
        long thresholdNum = 50000;
        System.out.printf("Begin Test : Current Map : %s , threshold  thread : %s\n", list.getClass(), threshold);
        for (int i = 0; i < 5; i++) {
            list.clear();
            long start = System.nanoTime();
            final AtomicInteger counter = new AtomicInteger();
            final ExecutorService executorService = Executors.newFixedThreadPool(threshold);
            for (int j = 0; j < threshold; j++) {
                executorService.execute(() -> {
                    for (int k = 0; k < thresholdNum && counter.getAndIncrement() < thresholdNum; k++) {
                        Integer num = (int) Math.ceil(Math.random() * 100000);
                        list.add(num);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(2, TimeUnit.MINUTES);
            long spend = (System.nanoTime() - start) / 1000000L;
            System.out.println("No:" + i + "-> Spend Time = " + spend + "ms");
            total += spend;
        }
        new Summary(threshold, total / 5).record(result);
        System.out.println("Spend Average Time =" + total / 5 + "ms");
    }
}
