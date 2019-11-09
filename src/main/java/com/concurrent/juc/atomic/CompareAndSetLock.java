package com.concurrent.juc.atomic;


import java.util.concurrent.atomic.AtomicInteger;

public class CompareAndSetLock {

    private final AtomicInteger value = new AtomicInteger();
    private Thread curThread;

    public void lock() throws GetLockException {
        final boolean success = value.compareAndSet(0, 1);
        if (!success)
            throw new GetLockException("Get Lock Failed");
        curThread = Thread.currentThread();
    }

    public void unlock() {
        if (curThread != Thread.currentThread() || value.get() == 0)
            return;
        value.compareAndSet(1, 0);
    }
}
