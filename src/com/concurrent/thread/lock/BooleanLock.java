package com.concurrent.thread.lock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 自定义显示锁
 */
public class BooleanLock implements Lock {

    private boolean isLocked;

    public BooleanLock() {
        this.isLocked = false;
    }

    private Collection<Thread> blockedThread = new ArrayList<>();

    private Thread currentThread;

    @Override
    public synchronized void lock() throws InterruptedException {
        while (isLocked) {
            blockedThread.add(Thread.currentThread());
            this.wait();
        }
        System.out.println(Thread.currentThread().getName() + " -> locked");
        blockedThread.remove(Thread.currentThread());
        this.isLocked = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void lock(long millis) throws InterruptedException, TimeOutException {
        if (millis <= 0)
            lock();
        long hasRemaining = millis;
        long endTime = System.currentTimeMillis() + millis;
        while (isLocked) {

            //在超时时间内没有获取到锁抛出超时异常
            if (hasRemaining <= 0)
                throw new TimeOutException("get lock timeout");
            blockedThread.add(Thread.currentThread());
            this.wait(millis);
            hasRemaining = endTime - System.currentTimeMillis();
        }
        blockedThread.remove(Thread.currentThread());
        this.isLocked = true;
        this.currentThread = Thread.currentThread();
    }

    @Override
    public synchronized void unlock() throws InterruptedException {
        //只有当前线程自己可以解锁
        if (currentThread == Thread.currentThread()) {
            System.out.println(Thread.currentThread().getName() + " -> release");
            this.notifyAll();
            this.isLocked = false;
        }
    }

    @Override
    public Collection<Thread> getBlockedThread() {
        //expose readOnly blocked Collection
        return Collections.unmodifiableCollection(blockedThread);
    }

    @Override
    public int getBlockedSize() {
        return blockedThread.size();
    }
}
