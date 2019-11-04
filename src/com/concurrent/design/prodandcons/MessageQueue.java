package com.concurrent.design.prodandcons;

import java.util.LinkedList;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 消息队列
 */
public class MessageQueue {

    private final LinkedList<Message> queue;
    private static final int DEFAULT_LIMIT = 100;
    private final int limit;

    public MessageQueue() {
        this(DEFAULT_LIMIT);
    }

    public MessageQueue(int limit) {
        this.limit = limit;
        this.queue = new LinkedList<>();
    }

    public void put(Message message) {
        synchronized (queue) {
            while (queue.size() > limit) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            queue.addLast(message);
            queue.notifyAll();
        }
    }

    public Message get() {
        synchronized (queue) {
            while (queue.isEmpty()) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            Message message = queue.removeFirst();
            queue.notifyAll();
            return message;
        }
    }

    public int getLimit() {
        return this.limit;
    }

    public int getQueueSize() {
        synchronized (queue) {
            return queue.size();
        }
    }
}
