package com.concurrent.juc;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * AtomicBooleanTest 和AtomicInteger类似，只不过用 0，1代替了false , true
 */
public class AtomicBooleanTest {


    private static final AtomicBoolean flag = new AtomicBoolean();

//    private static volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            while (!flag.get()) {
//                System.out.println("..");
            }
        }).start();

        Thread.sleep(2000);

        flag.set(true);
//        flag = true;
    }
}
