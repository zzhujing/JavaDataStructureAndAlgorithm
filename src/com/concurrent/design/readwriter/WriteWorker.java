package com.concurrent.design.readwriter;

import java.util.Random;


/**
 * 写工作线程
 */
public class WriteWorker extends Thread {

    private final Random random = new Random(System.currentTimeMillis());
    private final ShareData shareData;
    private final String filler;
    private int index;

    public WriteWorker(ShareData shareData, String filler) {
        this.shareData = shareData;
        this.filler = filler;
    }

    @Override
    public void run() {
        try {
            while (true) {
                char curChar = nextChar();
                shareData.write(curChar);
                //模拟延时
                Thread.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private char nextChar() {
        this.index++;
        if (index == filler.length()) index = 0;
        return filler.charAt(index);
    }
}
