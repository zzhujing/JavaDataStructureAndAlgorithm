package com.concurrent.juc.utils;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Phaser使用场景
 * 1. 在主线程中等待其他线程全部完成（arriveAndAwaitAdvance()），其中其他线程完成后不会阻塞（arrive）
 */
public class PhaserTest4 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(5);
        IntStream.rangeClosed(1, 4).boxed().forEach(i -> new PhaserArriveTask(phaser, i));

        //main thread await until all of other thread work done.
        phaser.arriveAndAwaitAdvance();
        System.out.println("all of 1 phase finished done.");
    }

    private static class PhaserArriveTask extends Thread {
        private final Phaser phaser;

        private PhaserArriveTask(Phaser phaser, int no) {
            super(String.valueOf(no));
            this.phaser = phaser;
            start();
        }

        @Override
        public void run() {
            try {
                System.out.println(getName() + " -> begin work.");
                TimeUnit.SECONDS.sleep(1);
                System.out.println(getName() + " -> end work.");
                //标记已经做完 继续其他任务
                phaser.arrive();

                //do other thing.
                TimeUnit.SECONDS.sleep(1);
                System.out.println(getName() + "-> finished done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
