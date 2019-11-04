package com.concurrent.design.suspension;

import java.util.Random;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ClientThread extends Thread {

    private final RequestQueue queue;
    private final Random random;
    private final String sendValue;

    public ClientThread(RequestQueue queue, String sendValue) {
        this.queue = queue;
        this.sendValue = sendValue;
        this.random = new Random(System.currentTimeMillis());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Request request = new Request(sendValue);
            queue.putRequest(request);
            System.out.println(Thread.currentThread().getName() + "-> send "+sendValue);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
