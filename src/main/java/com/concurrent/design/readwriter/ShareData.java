package com.concurrent.design.readwriter;

import java.util.Arrays;

/**
 * 共享数据
 */
public class ShareData {

    private final char[] data;
    private final ReadWriteLock lock = new ReadWriteLock();

    public ShareData(int size) {
        this.data = new char[size];
        initShareData(size);
    }

    private void initShareData(int size) {
        for (int i = 0; i < size; i++) {
            data[i] = '*';
        }
    }

    public char[] read() throws InterruptedException {
        try {
            lock.readLock();
            return doRead();
        } finally {
            lock.unReadLock();
        }
    }

    public void write(char c) throws InterruptedException {
        try {
            lock.writeLock();
            doWrite(c);
        } finally {
            lock.unWriteLock();
        }
    }

    private void doWrite(char c) throws InterruptedException {
        for (int i = 0; i < data.length; i++) {
            data[i] = c;
            Thread.sleep(10);
        }
    }

    private char[] doRead() {
        //直接将
        char[] result = new char[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = data[i];
        }
        return result;
    }

}
