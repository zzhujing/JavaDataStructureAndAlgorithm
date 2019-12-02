package com.concurrent;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义显示Lock
 */
public class MyLock {

    private Thread curThread;
    private final Set<Thread> waitThreadSet = new HashSet<>();
    private final Object lock = new Object();
    private volatile boolean isLock = false;

    public void lock() {
        synchronized (lock) {
            final Thread curThread = Thread.currentThread();
            while (isLock) {
                try {
                    waitThreadSet.add(curThread);
                    lock.wait();
                } catch (InterruptedException e) {
                    System.out.println("interrupt");
                }
            }
            waitThreadSet.remove(curThread);
            isLock = true;
            lock.notifyAll();
        }
    }

    public void lock(long millis) {
        if (millis < 0) lock();
        long start = System.currentTimeMillis();
        synchronized (lock) {
            Thread curThread = Thread.currentThread();
            while (isLock) {
                try {
                    if (System.currentTimeMillis() - start >= millis) {
                        throw new RuntimeException("timeout");
                    }
                    waitThreadSet.add(curThread);
                    lock.wait(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            waitThreadSet.remove(curThread);
            isLock = true;
            this.curThread = curThread;
            lock.notifyAll();
        }
    }

    public void unlock() {
        if (curThread != Thread.currentThread()) {
            return;
        }
        synchronized (lock) {
            while (!isLock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.isLock = false;
            lock.notifyAll();
        }
    }
}
