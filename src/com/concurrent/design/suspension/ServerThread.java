package com.concurrent.design.suspension;

import java.util.Random;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ServerThread extends Thread {

    private final RequestQueue queue;
    private final Random random;
    private volatile boolean closed = false;

    public ServerThread(RequestQueue queue) {
        this.queue = queue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        while (!closed) {
            Request request = queue.getRequest();
            if (request == null) {
                System.out.println("Received Empty Request..");
                continue;
            }
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                break;
            }
            System.out.println(Thread.currentThread().getName() + " Received ->" + request.getValue());
        }
    }

    public void close() {
        this.closed = true;
        this.interrupt();
    }
}
