package com.concurrent.design.countdown;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class CustomCountDown {

    private final int total;
    private int doneSize;

    public CustomCountDown(int size) {
        this.total = size;
        this.doneSize = 0;
    }

    public void down() {
        synchronized (this) {
            if (doneSize == total) {
                throw new IllegalArgumentException("Successful Size More than total");
            }
            doneSize++;
            this.notifyAll();
        }
    }

    public void await() throws InterruptedException {
        synchronized (this) {
            while (doneSize != total) {
                this.wait();
            }
        }
    }
}
