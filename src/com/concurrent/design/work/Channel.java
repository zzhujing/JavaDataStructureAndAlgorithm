package com.concurrent.design.work;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 模拟传送带
 */
public class Channel {

    private final Request[] requestQueue;
    private final WorkerThread[] workerPoll;
    private static final int MAX_REQUEST_NUMBER = 100;
    private int head;
    private int tail;
    private int count;

    public Channel(int workNumber) {
        this.requestQueue = new Request[MAX_REQUEST_NUMBER];
        this.workerPoll = new WorkerThread[workNumber];
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        init();
    }

    private void init() {

    }
}
