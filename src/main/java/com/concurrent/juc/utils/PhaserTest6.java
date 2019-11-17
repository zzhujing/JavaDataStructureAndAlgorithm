package com.concurrent.juc.utils;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Phaser 使用场景
 * 可中断的await,以及超时配置
 */
public class PhaserTest6 {
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(2);
        final Thread thread = new Thread(() -> {
            try {
                phaser.awaitAdvanceInterruptibly(phaser.getPhase(), 1, TimeUnit.SECONDS);
            } catch (InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        TimeUnit.SECONDS.sleep(10);
        thread.interrupt();
        System.out.println("main thread finished done .");
    }
}
