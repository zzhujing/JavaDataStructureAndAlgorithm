package com.concurrent.juc.utils;


import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Phaser -> 灵活整合了CountDownLatch 和CyclicBarrier
 * 1. 使用Pharse完成CountDownLatch
 */
public class PhaserTest1 {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {

        final Phaser phaser = new Phaser();

        IntStream.rangeClosed(1, 5)
                .boxed()
                .map(i -> phaser)
                .forEach(PhaserTask::new);

        phaser.register();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Main Thread work done.");

    }

    private static class PhaserTask extends Thread {

        private final Phaser phaser;

        public PhaserTask(Phaser phaser) {
            this.phaser = phaser;
            //动态注册parties
            phaser.register();
            start();
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "-> begin work ..");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(Thread.currentThread().getName() + "-> end work ..");

                //类似CyclicBarrier等待其他线程一起返回
                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
