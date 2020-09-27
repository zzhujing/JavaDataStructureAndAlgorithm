package com.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class
AsyncFuture<T> implements Future<T> {

    private volatile boolean isDone = false;
    private T result;

    @Override
    public T get() throws InterruptedException {
        synchronized (this) {
            while (!isDone) {
                this.wait();
            }
            return result;
        }
    }

    @Override
    public boolean isDone() {
        return isDone;
    }

    @Override
    public void done(T result) throws InterruptedException {
        synchronized (this) {
            this.isDone = true;
            this.result = result;
            this.notifyAll();
        }
    }

    static class Executor<T> {
        public <T> Future<T> execute(Runnable task, T result, Consumer<T> consumer) {
            AsyncFuture<T> future = new AsyncFuture<>();
            new Thread(() -> {
                try {
                    task.run();
                    future.done(result);
                    consumer.accept(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            return future;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Executor<String> executor = new Executor<>();
        final Future<String> future = executor.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "done", System.out::println);
        System.out.println("future build success");
        System.out.println(future.get());
        System.out.println("Main Thread Done.");
    }
}
