package com.concurrent.design.threadlocal;

import java.util.concurrent.ConcurrentHashMap;

public class CustomThreadLocal<T> {

    private ConcurrentHashMap<Thread, T> ctl = new ConcurrentHashMap<Thread, T>();

    public <T> T get() {
        return (T) ctl.get(Thread.currentThread());
    }

    public void set(T t) {
        ctl.put(Thread.currentThread(), t);
    }
}
