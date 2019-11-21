package com.concurrent.juc.threadpool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * future的缺陷
 * 1. 无法进行回调
 * 2. 执行批量任务的时候， 无法按加入的顺序进行返回值的获取
 * 3. java8之前有一个CompletionService用来处理第二种情况，在任务执行成功的时候将future放入一个阻塞队列中。
 */
public class FutureDefectTest {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testFutureDefectWithOrder();
    }

    private static void testFutureDefectWithNoCallback() throws ExecutionException, InterruptedException {
        final Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        //it will be block .
        future.get();
    }


    private static void testFutureDefectWithOrder() throws ExecutionException, InterruptedException {


        List<Callable<Integer>> callableList = Arrays.asList(
                () -> {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("5 finished.");
                    return 5;
                },
                () -> {
                    TimeUnit.SECONDS.sleep(4);
                    System.out.println("4 finished.");
                    return 4;
                },
                () -> {
                    TimeUnit.SECONDS.sleep(6);
                    System.out.println("6 finished.");
                    return 6;
                }
        );

        executorService.invokeAll(callableList).forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }
}
