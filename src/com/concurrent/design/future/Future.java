package com.concurrent.design.future;

public interface Future<T> {

    T get() throws InterruptedException;

    void done(T result) throws InterruptedException;

    boolean isDone();
}
