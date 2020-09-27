package com.concurrent.juc.threadpool;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * CompletableFuture Test 1 -> 演示和正常一般的线程池的不同
 * 1. 里面的线程池(ForkJoinPool)全都是守护线程
 * 2. 是ExecutorService和Future的结合体
 * 3. 可以完全异步并行的执行多种任务并最后自动完成回调
 */
public class CompletableFutureTest1 {
    public static void main(String[] args) throws InterruptedException {
//        testCompletableFutureFinishedNestingTaskList();
//        testCompletableFutureFirst();
        testExecutorServiceFinishedNestingTaskList2();
        //让主线程join住。否则CompletableFuture中线程池所有的线程为守护线程那么会随着主线程的结束而结束生命周期
        Thread.currentThread().join();
    }

    private static void testCompletableFutureFinishedNestingTaskList() {
        IntStream.range(0, 10).boxed()
                .forEach(i -> CompletableFuture
                        .supplyAsync(CompletableFutureTest1::get)
                        .thenAccept(CompletableFutureTest1::display)
                        .whenComplete((v, t) -> System.out.println(i + "DONE."))
                );
    }

    private static void testCompletableFutureFirst() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin task ...");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("finished done.");
                return "DONE";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((r, t) -> System.out.println(r));
    }

    private static void testExecutorServiceFinishedNestingTaskList1() {
        //测试使用原生的线程池完成嵌套的任务(在future.get()之后继续进行复杂任务)
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        //若使用parallelStream()进行调度那么则会异步执行display，不会被其他的future#get()影响
        IntStream.range(0, 10).boxed().map(i -> executorService.submit((Callable<Integer>) CompletableFutureTest1::get)).
                map(f -> {
                    try {
                        return f.get();
                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }).parallel().forEach(CompletableFutureTest1::display);
    }

    private static void testExecutorServiceFinishedNestingTaskList2() throws InterruptedException {
        //测试使用原生的线程池完成嵌套的任务(在future.get()之后继续进行复杂任务)
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final List<Callable<Integer>> tasks = IntStream.range(0, 10).boxed().map(i -> (Callable<Integer>) CompletableFutureTest1::get).collect(Collectors.toList());
        //阻塞的
        executorService.invokeAll(tasks).parallelStream().map(f -> {
            try {
                return f.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }).forEach(CompletableFutureTest1::display);
    }

    private static int get() {
        try {
            final int random = ThreadLocalRandom.current().nextInt(20);
            System.out.println(getCurrentThreadName() + " -> come into get task .");
            System.out.printf(getCurrentThreadName() + "-> will be into sleep -> [%s]s\n", random);
            TimeUnit.SECONDS.sleep(random);
            System.out.printf(getCurrentThreadName() + " -> get the value %s \n", random);
            return random;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void display(int v) {
        try {
            final int random = ThreadLocalRandom.current().nextInt(20);
            System.out.println(getCurrentThreadName() + " -> come into display");
            System.out.printf(getCurrentThreadName() + " -> will be into sleep -> [%s]s\n", random);
            TimeUnit.SECONDS.sleep(random);
            System.out.printf(getCurrentThreadName() + " -> display the value %s \n", random);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentThreadName() {
        return Thread.currentThread().getName();
    }
}
