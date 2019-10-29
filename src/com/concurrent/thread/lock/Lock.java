package com.concurrent.thread.lock;

import java.util.Collection;

/**
 * 自定义锁
 */
public interface Lock {

    /**
     * 自定义超时异常
     */
    class TimeOutException extends Exception {
        public TimeOutException(String message) {
            super(message);
        }
    }

    /**
     * 获取锁
     */
    void lock() throws InterruptedException;

    /**
     * 带超时功能的获取锁
     *
     * @param millis 超时时间
     */
    void lock(long millis) throws InterruptedException, TimeOutException;

    /**
     * 释放锁
     */
    void unlock() throws InterruptedException;

    /**
     * 返回所有的被阻塞的线程集合
     */
    Collection<Thread> getBlockedThread();

    /**
     * 线程集合个数
     */
    int getBlockedSize();
}
