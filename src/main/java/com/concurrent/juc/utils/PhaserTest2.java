package com.concurrent.juc.utils;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 演示Phaser的重复使用 -> Phaser会在达到初始化的分区数量后自动reset()
 * <p>
 * {@link Phaser#getPhase()}  获取当前所在的阶段，没进行完一轮await就会自增1
 * {@link Phaser#getArrivedParties()} 获取当前已经完成的分区数
 */
public class PhaserTest2 {
    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {

        //初始化Phaser的分区数
        final Phaser phaser = new Phaser(5);

        IntStream.rangeClosed(1, 5).boxed().forEach(i -> new SportMan(phaser, i.toString()));
    }

    private static class SportMan extends Thread {

        private final Phaser phaser;
        private final String no;

        private SportMan(Phaser phaser, String no) {
            this.phaser = phaser;
            this.no = no;
            start();
        }

        /**
         * 等到分区数到达初始值的的时候等待结束，且分片区自动重置
         */
        @Override
        public void run() {
            try {

                //phase 1
                System.out.println(Thread.currentThread().getName() + "-> begin running");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(Thread.currentThread().getName() + "-> end running");
                System.out.println(phaser.getPhase());
                phaser.arriveAndAwaitAdvance();

                //automatic reset
                //phase 2
                System.out.println(Thread.currentThread().getName() + "-> begin bicycle");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(Thread.currentThread().getName() + "-> end bicycle");
                System.out.println(phaser.getPhase());
                phaser.arriveAndAwaitAdvance();

                //phase 3
                System.out.println(Thread.currentThread().getName() + "-> begin jump");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(Thread.currentThread().getName() + "-> end jump");
                System.out.println(phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
