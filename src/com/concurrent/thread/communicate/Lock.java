package com.concurrent.thread.communicate;

import java.util.Collection;

public interface Lock {

    class TimeOutException extends Exception {
        public TimeOutException(String message) {
            super(message);
        }
    }

    void lock() throws InterruptedException;

    void lock(long millis) throws InterruptedException, TimeOutException;

    void unlock() throws InterruptedException;

    Collection<Thread> getBlockedThread();

    int getBlockedSize();
}
