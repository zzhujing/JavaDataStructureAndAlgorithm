package com.concurrent.juc.utils;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * Phaser Api Test
 */
public class PhaserTest3 {

    private static final Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {
        final Phaser phaser = new Phaser(2) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                //返回false则会重置parties,否则则不会TimeUnit.SECONDS.sleep(random.nextInt(5));
                return false;
            }
        };
        new Thread(new AdvanceTask(phaser, "hj")).start();
        new Thread(new AdvanceTask(phaser, "xcc")).start();
    }

    private static class AdvanceTask extends Thread {
        private final Phaser phaser;

        private AdvanceTask(Phaser phaser, String name) {
            super(name);
            this.phaser = phaser;
        }

        @Override
        public void run() {
            try {
                System.out.println(getName() + " -> begin");
                System.out.println(phaser.getPhase());
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(getName() + " -> end");
                phaser.arriveAndAwaitAdvance();

                System.out.println(getName() + " -> begin second");
                System.out.println(phaser.getPhase());
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(getName() + " -> end second");
                phaser.arriveAndAwaitAdvance();

                System.out.println(getName() + " -> begin third");
                System.out.println(phaser.getPhase());
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(getName() + " -> end third");
                phaser.arriveAndAwaitAdvance();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
