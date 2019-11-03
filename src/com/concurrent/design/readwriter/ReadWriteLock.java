package com.concurrent.design.readwriter;

/**
 * 自定义读写锁
 */
public class ReadWriteLock {

    private int readingReader;
    private int waitingReader;
    private int writingWriter;
    private int waitingWriter;
    private boolean preferWrite;

    public ReadWriteLock() {
        this(true);
    }

    public ReadWriteLock(boolean preferWrite) {
        this.preferWrite = preferWrite;
    }

    public synchronized void readLock() throws InterruptedException {
        this.waitingReader++;
        try {
            while (writingWriter > 0 || (preferWrite && waitingWriter > 0)) {
                this.wait();
            }
            this.readingReader++;
        } finally {
            this.waitingReader--;
        }
    }

    public synchronized void unReadLock() {
        this.readingReader--;
        this.notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        this.waitingWriter++;
        try {
            while (readingReader > 0 || writingWriter > 0) {
                this.wait();
            }
            this.writingWriter++;
        } finally {
            this.waitingWriter--;
        }
    }

    public synchronized void unWriteLock() {
        this.writingWriter--;
        this.notifyAll();
    }


}
