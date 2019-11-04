package com.concurrent.design.activeobject;

import java.util.LinkedList;

/**
 * 储存所有的MethodRequest
 */
public class ActivationQueue {

    private final LinkedList<MethodRequest> requestQueue;

    private static final int MAX_METHOD_REQUEST_QUEUE_SIZE = 100;

    public ActivationQueue() {
        requestQueue = new LinkedList<>();
    }

    public synchronized void put(MethodRequest request) {
        while (requestQueue.size() >= MAX_METHOD_REQUEST_QUEUE_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        requestQueue.addLast(request);
        this.notifyAll();
    }

    public synchronized MethodRequest take() {
        while (requestQueue.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        MethodRequest request = requestQueue.removeFirst();
        this.notifyAll();
        return request;
    }
}
