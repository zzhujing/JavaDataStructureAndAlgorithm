package com.concurrent;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MapPerformanceTest {

    private static final List<Summary> result = Lists.newArrayList();

    public static void main(String[] args) throws InterruptedException {
/*
        performance(new Hashtable<>(), 1, false);
        performance(new Hashtable<>(), 1, true);

        performance(new ConcurrentHashMap<>(), 1, false);
        performance(new ConcurrentHashMap<>(), 1, true);

        performance(Collections.synchronizedMap(new HashMap<>()), 1, false);
        performance(Collections.synchronizedMap(new HashMap<>()), 1, true);
        System.out.println("=======================================");
*/

        IntStream.rangeClosed(1, 10).forEach(i -> {
            try {
                performance(new ConcurrentHashMap<>(), i * 10);
                performance(new ConcurrentSkipListMap<>(), i * 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        result.forEach(System.out::println);
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


    /**
     * threshold 数量线程插入500000 数据的平均时间花费
     */
    public static void performance(Map<String, Integer> map, int threshold) throws InterruptedException {
        long total = 0;
        long thresholdNum = 5000000;
        System.out.printf("Begin Test : Current Map : %s , threshold  thread : %s\n", map.getClass(), threshold);
        for (int i = 0; i < 5; i++) {
            map.clear();
            long start = System.nanoTime();
            final AtomicInteger counter = new AtomicInteger();
            final ExecutorService executorService = Executors.newFixedThreadPool(threshold);
            for (int j = 0; j < threshold; j++) {
                executorService.execute(() -> {
                    for (int k = 0; k < thresholdNum && counter.getAndIncrement() < thresholdNum; k++) {
                        Integer num = (int) Math.ceil(Math.random() * 100000);
                        map.get(String.valueOf(num));
                        map.put(String.valueOf(num), num);
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
