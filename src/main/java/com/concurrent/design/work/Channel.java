package com.concurrent.design.work;

import java.util.Arrays;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 模拟传送带
 */
public class Channel {

    private final Request[] requestQueue;
    private final WorkerThread[] workerPool;
    private static final int MAX_REQUEST_NUMBER = 100;
    private int head;
    private int tail;
    private int count;

    public Channel(int workNumber) {
        this.requestQueue = new Request[MAX_REQUEST_NUMBER];
        this.workerPool = new WorkerThread[workNumber];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        init();
    }

    private void init() {
        for (int i = 0; i < workerPool.length; i++) {
            workerPool[i] = new WorkerThread("WORKER : " + i, this);
        }
    }

    public void start() {
        Arrays.stream(workerPool).forEach(WorkerThread::start);
    }

    public synchronized void put(Request request) {
        while (count >= MAX_REQUEST_NUMBER) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                break;
            }
        }
        requestQueue[tail] = request;
        tail = (tail + 1) % requestQueue.length;
        count++;
        this.notifyAll();
    }

    public synchronized Request take() {
        while (count == 0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                break;
            }
        }
        Request request = requestQueue[head];
        this.head = (head+1) % requestQueue.length;
        count--;
        this.notifyAll();
        return request;
    }
}
