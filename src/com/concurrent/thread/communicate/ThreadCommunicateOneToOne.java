package com.concurrent.thread.communicate;


/**
 * 使用简单的生产者消费者模式来进行线程之间通信test,
 * 1对1 的时候完全没有问题
 */
public class ThreadCommunicateOneToOne {
    public static void main(String[] args) {

        ThreadCommunicateOneToOne test  = new ThreadCommunicateOneToOne();
         new Thread(()->{
             while (true) test.produce();
         }).start();
         new Thread(()->{
             while (true) test.consume();
         }).start();
    }


    private final Object LOCK = new Object();
    private volatile boolean isProduce = false;
    private int i = 0;

    public void produce() {
        synchronized (LOCK) {
            if (isProduce) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("P -> " + (++i));
                LOCK.notify();
                isProduce = true;
            }
        }
    }

    public void consume() {
        synchronized (LOCK) {
            if (isProduce) {
                System.out.println("C ->" + i);
                //消费完毕通知生产
                LOCK.notify();
                isProduce = false;
            } else {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
