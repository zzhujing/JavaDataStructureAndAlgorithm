package com.concurrent.design.readwriter;

/**
 * 读工作线程
 */
public class ReadWorker extends Thread {

    private final ShareData shareData;

    public ReadWorker(ShareData shareData) {
        this.shareData = shareData;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(Thread.currentThread().getName() + " reads " + String.valueOf(shareData.read()));
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
