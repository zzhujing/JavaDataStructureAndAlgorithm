package com.concurrent.design.suspension;

import java.util.LinkedList;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 模拟请求队列
 */
public class RequestQueue {

    private final LinkedList<Request> queue = new LinkedList<>();

    public Request getRequest() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    //打断直接返回null, break的话又会进行判断等待
                    return null;
                }
            }
            return queue.removeFirst();
        }
    }

    public void putRequest(Request request) {
        synchronized (queue) {
            queue.addLast(request);
            queue.notifyAll();
        }
    }

}
