package com.concurrent.juc.utils;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Phaser 使用场景
 * 可中断的await,以及超时配置
 */
public class PhaserTest7 {
    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(2);
        final Thread thread = new Thread(phaser::arriveAndAwaitAdvance);
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(phaser.isTerminated());
        phaser.forceTermination();
        System.out.println(phaser.isTerminated());
    }
}
