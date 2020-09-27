package com.concurrent.juc.utils;

import com.algorithm.SortDemo;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 使用ForkJoin完成 快速排序
 */
public class ForkJoinQuickSortTest {
    public static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        int[] data = new int[1_000_000_0];

        for (int i = 0; i < 1_000_000_0; i++) {
            data[i] = random.nextInt(1000);
        }
//        testForkJoin(data); //5441ms
        long startTime = System.currentTimeMillis();
//        testSingleThread(data); //41743ms
        Arrays.sort(data); //还是jdk的快 640ms
        System.out.println((System.currentTimeMillis() - startTime) + "ms");
    }

    private static void testSingleThread(int[] data) {
        SortDemo.quickSort(data, data.length);
    }

    private static void testForkJoin(int[] data) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final QuickSortRecursiveAction task = new QuickSortRecursiveAction(data);
        long startTime = System.currentTimeMillis();
        forkJoinPool.execute(task);
        try {
            forkJoinPool.shutdown();
            forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
            System.out.println((System.currentTimeMillis() - startTime) + "ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static class QuickSortRecursiveAction extends RecursiveAction {

        private int[] data;
        private final int begin;
        private final int end;
        private static final int MAX_THRESHOLD = 30;

        private QuickSortRecursiveAction(int[] data) {
            this.data = data;
            this.begin = 0;
            this.end = data.length - 1;
        }

        private QuickSortRecursiveAction(int[] data, int begin, int end) {
            this.data = data;
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - begin <= MAX_THRESHOLD) {
                Arrays.sort(data, begin, end + 1);
            } else {
                int pivot = pivotFunc(data, begin, end);
                //calculate the pivot index
                QuickSortRecursiveAction left = new QuickSortRecursiveAction(data, begin, pivot - 1);
                QuickSortRecursiveAction right = new QuickSortRecursiveAction(data, pivot + 1, end);
                left.fork();
                right.fork();
            }
        }

        private int pivotFunc(int[] data, int begin, int end) {
            int pivot = data[end];
            int minFlagIndex = begin;
            for (int i = begin; i < end; i++) {
                if (data[i] < pivot) {
                    //交换begin和i
                    swap(data, minFlagIndex++, i);
                }
            }
            swap(data, minFlagIndex, end);
            return minFlagIndex;
        }

        private void swap(int[] source, int i, int j) {
            int tmp = source[i];
            source[i] = source[j];
            source[j] = tmp;
        }
    }
}
