package com.concurrent.thread.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * {@link Timer} 示例
 **/
public class TimerDemo {
    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    //ignore
                }
                System.out.println("Hello,Timer Demo!");
            }
        },2000,1000);
    }
}
