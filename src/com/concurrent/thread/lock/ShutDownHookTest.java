package com.concurrent.thread.lock;

import java.io.IOException;

/**
 * @author : hujing
 * @date : 2019/10/29
 * 测试ShutDownHook
 */
public class ShutDownHookTest {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("shutdown Hook invoked!");
            notifyAndRelease();
        }));
        int no = 0;
        while (no < 20) {
            Thread.sleep(1000);
            System.out.println("i'm working.");
            no++;
        }
        throw new RuntimeException("error");
    }

    private static void notifyAndRelease() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("close file ,connection,release io");
    }
}
