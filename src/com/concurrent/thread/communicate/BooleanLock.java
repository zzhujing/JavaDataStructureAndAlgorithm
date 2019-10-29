package com.concurrent.thread.communicate;

import java.util.ArrayList;
import java.util.Collection;

public class BooleanLock implements Lock {
    private boolean isLocked;

    public BooleanLock() {
        this.isLocked = false;
    }

    private Collection<Thread> blockedThread = new ArrayList<>();

    @Override
    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            blockedThread.add(Thread.currentThread());
            this.wait();
        }
        blockedThread.remove(Thread.currentThread());
        this.isLocked = true;
    }

    @Override
    public void lock(long millis) throws InterruptedException, TimeOutException {
        while (isLocked) {
            blockedThread.add(Thread.currentThread());
            this.wait(millis);
        }
        blockedThread.remove(Thread.currentThread());
        this.isLocked = true;
    }

    @Override
    public synchronized void unlock() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " -> release");
        this.isLocked = false;
        this.notifyAll();
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        return blockedThread;
    }

    @Override
    public int getBlockedSize() {
        return blockedThread.size();
    }
}
