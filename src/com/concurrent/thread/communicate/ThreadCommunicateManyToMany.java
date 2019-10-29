package com.concurrent.thread.communicate;


import java.util.stream.Stream;

/**
 * 使用简单的生产者消费者模式来进行线程之间通信test,
 * 多对多的时候 notify不知道去唤醒那个线程，可能consume唤醒其他consume那么这样很容易会全部等待。
 * 如何解决？
 * 使用notifyAll()唤醒全部等待线程，这时候要注意，唤醒之后必须要通过判断状态去生产或者消费数据，否则会出现重复生产或者消费问题
 */
public class ThreadCommunicateManyToMany {
    public static void main(String[] args) {

        ThreadCommunicateManyToMany test = new ThreadCommunicateManyToMany();
        Stream.of("P1", "P2").forEach(p -> {
            new Thread(() -> {
                while (true) test.produce();
            }, p).start();
        });
        Stream.of("C1", "C2").forEach(c -> {
            new Thread(() -> {
                while (true) test.consume();
            }, c).start();
        });
    }


    private final Object LOCK = new Object();
    private volatile boolean isProduce = false;
    private int i = 0;

    public void produce() {
        synchronized (LOCK) {
            //这里使用while是为了避免重复生产数据问题
            while (isProduce) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("P -> " + (++i));
            LOCK.notifyAll();
            isProduce = true;
        }
    }

    public void consume() {
        synchronized (LOCK) {
            while (!isProduce) {
                try {
                    LOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("C ->" + i);
            //消费完毕通知生产
            LOCK.notifyAll();
            isProduce = false;
        }
    }
}
