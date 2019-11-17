package com.concurrent.juc.utils;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser 使用场景
 * 使用awaitAdvance()监控所有任务是否完成。但是自己不会占用parties.
 */
public class PhaserTest5 {
    public static void main(String[] args) {
        final Phaser phaser = new Phaser(3);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " -> begin work.");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(Thread.currentThread().getName() + " -> end work.");
                    phaser.arriveAndAwaitAdvance();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        //不占用parties等待所有任务arrive。该方法需要传入当前的phase阶段，如果不对则不会block等待
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("all of parties finished done .");
    }
}
