package com.concurrent.design.future;

import java.util.function.Consumer;

public class FutureService<T> {

    //不带回调的future
    public <T> Future<T> submit(final FutureTask<T> task) {
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        new Thread(() -> {
            T result = task.call(); //异步执行任务
            asyncFuture.done(result); //执行完毕回调
        }).start();
        return asyncFuture;
    }

    //带有回调通知的Future
    public <T> Future<T> submit(final FutureTask<T> task, final Consumer<T> consumer) {
        AsyncFuture<T> asyncFuture = new AsyncFuture<>();
        new Thread(() -> {
            T result = task.call(); //异步执行任务
            asyncFuture.done(result); //执行完毕回调
            consumer.accept(result);
        }).start();
        return asyncFuture;
    }
}
