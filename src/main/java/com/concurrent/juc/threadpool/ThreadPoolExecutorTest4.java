package com.concurrent.juc.threadpool;

import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class ThreadPoolExecutorTest4 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testInvokeAny();
//        testInvokeAll();
//        testSubmit();
        testPrivateAddTaskToQueue();
    }

    private static void testInvokeAny() throws ExecutionException, InterruptedException, TimeoutException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final Integer value = executorService.invokeAny(IntStream.rangeClosed(1, 5).mapToObj(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            return i;
        }).collect(toList()), 5, TimeUnit.SECONDS);
        System.out.println("----------is block? ---------");
        System.out.println(value);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }


    private static void testInvokeAll() throws ExecutionException, InterruptedException, TimeoutException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.invokeAll(IntStream.rangeClosed(1, 5).mapToObj(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            return i;
        }).collect(toList()), 10, TimeUnit.SECONDS).stream().map(future -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }).forEach(System.out::println);
        System.out.println("----------is block? ---------");
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);
    }


    private static void testSubmit() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
                System.out.println("test submit()...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "DONE");

        System.out.println("do some other things...");
        System.out.println(future.get());
    }

    private static void testPrivateAddTaskToQueue() throws InterruptedException {
        final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        threadPoolExecutor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("do task.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        TimeUnit.SECONDS.sleep(1);
        //可以通过直接在线程池的任务队列中添加任务。不过前提是要有活跃的worker线程。
        threadPoolExecutor.getQueue().add(() -> System.out.println("private runnable task ."));
    }
}


