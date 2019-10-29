package com.concurrent.thread.communicate;

import java.util.stream.Stream;

/**
 * @author : hujing
 * @date : 2019/10/28
 * wait()和Sleep()方法的区别
 * - `wait()`会释放当前锁到锁队列中,`sleep()`不会
 * - 使用`wait()`的时候当前线程必须是锁的持有者,`sleep()`不需要
 */
public class DifferenceOfWaitAndSleep {

    public static void main(String[] args) {
        //wait()在阻塞的时候会释放锁
        Stream.of("t1", "t2").forEach(name -> {
            new Thread(DifferenceOfWaitAndSleep::m2, name).start();
        });

        //sleep()在阻塞的时候不会释放锁
        Stream.of("t1", "t2").forEach(name -> {
            new Thread(DifferenceOfWaitAndSleep::m1, name).start();
        });
    }

    public static void m1() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void m2() {
        try {
            System.out.println("m2 invoked!");
            //此时必须使用synchronized加锁，不然会报错
            DifferenceOfWaitAndSleep.class.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
