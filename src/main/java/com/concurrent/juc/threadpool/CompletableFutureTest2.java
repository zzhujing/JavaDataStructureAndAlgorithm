package com.concurrent.juc.threadpool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture Test 2 -> 创建CompletableFuture
 * 1. runAsync()
 * 2. supplyAsync()
 * 3. anyOf()
 * 4. allOf()
 * 5. completableFuture()
 *
 * 几个Api
 * thenAccept() / thenAcceptAsync() -> 接受一个Consumer
 * whenComplete() / whenCompleteAsync() -> 完成的时候执行
 * runAfterBoth() / runAfterBothAsync() -> 异步进行两个CompletableFuture
 */
public class CompletableFutureTest2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final Future<?> future = allOf();
        System.out.println("->" + future.get());
        Thread.currentThread().join();
    }

    public static Future<?> supplyAsync() {
        //提交一个Runnable

        return CompletableFuture.supplyAsync(Object::new)
                .thenAcceptAsync(o -> {
                    try {
                        System.out.println("begin task 2." + o);
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .whenComplete((v, t) -> System.out.println("finished task 2"))
                //异步执行另一个任务
                .runAfterBothAsync(
                        //提交一supply
                        CompletableFuture.supplyAsync(() -> "hello")
                                .thenAcceptAsync(
                                        s -> {
                                            try {
                                                System.out.println("begin task 1.");
                                                TimeUnit.SECONDS.sleep(5);
                                            } catch (InterruptedException e) {
                                                throw new RuntimeException(e);
                                            }
                                        })
                                .whenComplete((v, t) -> System.out.println("finished task 1")),
                        () -> System.out.println("all of completable future finished done.")
                );
    }

    /**
     * 返回的是CompletableFuture<Object>
     */
    public static Future<?> anyOf() {
        return CompletableFuture.anyOf(
                CompletableFuture
                        .runAsync(() -> {
                            try {
                                System.out.println("begin task .");
                                TimeUnit.SECONDS.sleep(5);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        })
                        .whenComplete((v, t) -> System.out.println("task 1 finished!"))
                ,
                CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                System.out.println("begin task2 .");
                                TimeUnit.SECONDS.sleep(3);
                                return "FINISHED";
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .whenComplete((v, t) -> System.out.println("task 2 finished!"))
        );
    }


    /**
     * 返回的是CompletableFuture<Void>
     */
    public static Future<?> allOf() {
        return CompletableFuture.allOf(
                CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                System.out.println("begin task .");
                                TimeUnit.SECONDS.sleep(5);
                                return "task 1 finished!";
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .whenComplete((v, t) -> System.out.println("task 1 finished!"))
                ,
                CompletableFuture
                        .supplyAsync(() -> {
                            try {
                                System.out.println("begin task2 .");
                                TimeUnit.SECONDS.sleep(3);
                                return "FINISHED";
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .whenComplete((v, t) -> System.out.println("task 2 finished!"))
        );
    }
}
