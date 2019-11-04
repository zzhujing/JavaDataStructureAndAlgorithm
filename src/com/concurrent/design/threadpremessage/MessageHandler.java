package com.concurrent.design.threadpremessage;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class MessageHandler {

    private static final Random random = new Random(System.currentTimeMillis());
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public void handler(Message message) {
        executorService.execute(() -> {
            String value = message.getValue();
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " -> " + value);
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
