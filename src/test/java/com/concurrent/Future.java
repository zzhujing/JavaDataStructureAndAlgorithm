package com.concurrent;

public interface Future<T> {

    T get() throws InterruptedException;

    boolean isDone();

    void done(T result) throws InterruptedException;

}
