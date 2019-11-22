package com.concurrent.juc.threadpool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * CompletableFuture Method.
 * 1. create (already perform)
 * - supplyAsync()
 * - runAsync()
 * - anyOf()                           //执行任意一个
 * - allOf()                           //执行所有
 * - completableFuture()                //将一个泛型值包装成CompletableFuture.
 * 2. transfer
 * to T
 * - whenComplete(BiConsumer)          //完成时执行可以获取到上一阶段的Future返回值和异常t
 * - thenApply(Function())              //执行完进行Function转化
 * - handler(BiFunction(T,Throwable))  // 相比thenApply可以获取到异常。
 * - toCompletedFuture()               //返回自身
 * to void
 * - thenAccept(Consumer)             //消费
 * - thenRun(Runnable action)         //最后执行一个Runnable的action
 * * 3. compose
 * thenAcceptBoth()                //同时执行两个Future , 并且会对两执行结果进行BiConsumer消费
 * acceptEither()                  //同时执行两个Future，并且选择返回一个执行最后的consumer方法
 * runAfterBoth()                 //同时执行两个future，并且两方法执行完之后会执行一个Runnable的action
 * runAfterEight()                 //同时执行两个future，并且两方法中任意一个执行完之后会执行一个Runnable的action，此时另外一个任务仍然会得到执行
 * thenCombiner()                  //两future通过BiFunction生成新的future , 和thenAcceptBoth()差别在于最后的合并函数一个是BiConsumer，一个是BiFunction
 * thenCompose()                  //前面future的返回值会作为后面组合的future的入参。会等待前面的future执行完毕
 * <p>
 * 4. terminal
 * - getNow(T value)                       //getNow()如果future没有结束则返回value。结束则返回future的返回值或者抛出异常。
 * - join()                        // 使用unchecked包装方法。相当于不抛checkedException的get()
 * - complete()                   //提前设置返回值，如果成功。那么Future#get()将返回提前设置值。 解决future#get()block的缺点 ,
 * - completeExceptionally()       //不能提前使用future#get()获取值，获取就抛出异常
 * - obtrudeException() ？obtrudeValue()      //直接抛出异常结束future /直接返回一个值
 * - exceptionally(Function<Throwable,String>)   //处理future中发生的异常
 */
public class CompletableFutureTest3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        testWhenComplete();
//        testTransferMethod();
//        testThenAcceptBoth();
//        testAcceptEither();
//        testRunAfterBoth();
//        testRunAfterEither();
//        testThenCombine();
//        testThenCompose();
//        testGetNow();
//        testJoin();
//        testComplete();
        testCompletionExceptionally();
        Thread.currentThread().join();
    }


    private static void testCompletionExceptionally() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                int i = 1 / 0;
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).exceptionally(Throwable::getMessage);
//        future.completeExceptionally(new RuntimeException("error"));
        future.obtrudeValue("result");
        System.out.println(future.get());
        future.obtrudeException(new RuntimeException("error"));
        System.out.println(future.get());
    }

    private static void testComplete() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        TimeUnit.SECONDS.sleep(3);
        System.out.println(future.complete("World"));
        System.out.println(future.get());
    }


    private static void testJoin() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(future.join());
    }

    private static void testGetNow() {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        System.out.println(future.getNow("HelloWorld"));
    }


    private static void testThenCompose() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenComposeAsync(s ->
                CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("begin thenAcceptBoth -1");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("end thenAcceptBoth -1");
                        return s.length();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
        ).thenAccept(System.out::println);
    }

    private static void testThenCombine() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenCombine(
                CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("begin thenAcceptBoth -1");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("end thenAcceptBoth -1");
                        return 200;
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }),
                (s, i) -> s.length() > i
        ).thenAccept(System.out::println);
    }


    private static void testThenAcceptBoth() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenAcceptBoth(
                CompletableFuture.supplyAsync(() -> {
                            try {
                                System.out.println("begin thenAcceptBoth -2");
                                TimeUnit.SECONDS.sleep(2);
                                System.out.println("end thenAcceptBoth -2");
                                return 100;
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ),
                (s, i) -> System.out.println(s + "_ " + i));
    }

    private static void testRunAfterEither() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).runAfterEither(
                CompletableFuture.supplyAsync(() -> {
                            try {
                                System.out.println("begin thenAcceptBoth -2");
                                TimeUnit.SECONDS.sleep(2);
                                System.out.println("end thenAcceptBoth -2");
                                return 100;
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ), () -> System.out.println("finished!"));
    }

    private static void testRunAfterBoth() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).runAfterBoth(
                CompletableFuture.supplyAsync(() -> {
                            try {
                                System.out.println("begin thenAcceptBoth -2");
                                TimeUnit.SECONDS.sleep(2);
                                System.out.println("end thenAcceptBoth -2");
                                return "100";
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ), () -> System.out.println("finished."));
    }

    private static void testAcceptEither() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("begin thenAcceptBoth -1");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("end thenAcceptBoth -1");
                return "Hello";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).acceptEither(
                CompletableFuture.supplyAsync(() -> {
                            try {
                                System.out.println("begin thenAcceptBoth -2");
                                TimeUnit.SECONDS.sleep(2);
                                System.out.println("end thenAcceptBoth -2");
                                return "100";
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ), System.out::println);
    }


    private static void testWhenComplete() throws InterruptedException, ExecutionException {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello")
                // 这里的xxxAsync() 指的是执行这些方法的时候是异步的不会block,所以会执行执行下面Main线程中的语句
                // 如果要想提前使用前面返回的future，那么需要先获取到前面的future。而不能链式编程在一起。
                .whenCompleteAsync
                        ((v, t) -> {
                            try {
                                System.out.println("begin complete");
                                TimeUnit.SECONDS.sleep(5);
                                System.out.println("over complete");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
        System.out.println("is block ?");
        System.out.println(future.get());
    }

    private static void testTransferMethod() throws ExecutionException, InterruptedException {
        final CompletableFuture<Void> future = CompletableFuture.supplyAsync(() -> "Hello")
//                .thenApplyAsync(String::length);
//                .handleAsync((s, t) -> {
//                    if (t != null) {
//                        System.out.println(t.getMessage());
//                        return 0;
//                    } else {
//                        return s.length();
//                    }
//                });
                .whenComplete((s, t) -> System.out.println(s.length()))
                .thenRun(() -> System.out.println("finished done."));
        System.out.println("is block ?");
        System.out.println(future.get());
    }
}
