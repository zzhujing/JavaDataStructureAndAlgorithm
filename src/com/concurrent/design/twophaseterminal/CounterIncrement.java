package com.concurrent.design.twophaseterminal;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class CounterIncrement extends Thread {

    private volatile boolean running = true;
    private int counter = 0;

    @Override
    public void run() {
        try {
            while (running) {
                System.out.println(Thread.currentThread().getName() + "  counter -> " + counter++);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            //ignore
        } finally {
            this.clean();
        }
    }

    public void clean() {
        System.out.println("clean counterIncrement");
    }

    public void close() {
        this.running = false;
        this.interrupt();
    }
}
