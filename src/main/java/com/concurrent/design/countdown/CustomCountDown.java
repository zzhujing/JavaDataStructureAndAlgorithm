package com.concurrent.design.countdown;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class CustomCountDown {

    private final int total;
    private AtomicInteger doneSize;
    private final long timeoutMillis;

    public CustomCountDown(int size, long timeout) {
        this.total = size;
        this.timeoutMillis = System.currentTimeMillis() + timeout;
        this.doneSize = new AtomicInteger(0);
    }

    public void down() {
        doneSize.incrementAndGet();
    }

    public void await() throws InterruptedException {
        while (!doneSize.compareAndSet(total, 0)) {
            if (System.currentTimeMillis() >= timeoutMillis) throw new RuntimeException("timeout");
        }
    }
}
