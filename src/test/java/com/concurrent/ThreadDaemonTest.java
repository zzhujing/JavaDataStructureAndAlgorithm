package com.concurrent;

import java.util.concurrent.TimeUnit;

public class ThreadDaemonTest {
    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        service.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        service.shutdown(3000);
        System.out.println("Main Thread done.");
    }

    static class ThreadService {
        private Thread executeThread;
        volatile boolean flag = false;

        void execute(Runnable task) {
            executeThread = new Thread(() -> {
                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
                try {
                    t.join();
                    flag = true;
                } catch (InterruptedException e) {
                    //ignore
                }
            });
            executeThread.start();
        }

        public void shutdown(long millis) {
            long start = System.currentTimeMillis();
            while (!flag) {
                if ((System.currentTimeMillis() - start) >= millis) {
                    executeThread.interrupt();
                    flag = true;
                    break;
                }
            }
        }
    }
}
