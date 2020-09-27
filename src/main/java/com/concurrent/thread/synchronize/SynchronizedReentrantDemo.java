package com.concurrent.thread.synchronize;

/**
 * 测试Synchronized 的可重入性
 **/
public class SynchronizedReentrantDemo {
    public static void main(String[] args) {
        m1();
    }


    public synchronized static void m1() {
        System.out.println("come into m1..");
        m2();
        System.out.println("Sign Out m1..");
    }

    public synchronized static void m2() {
        System.out.println("come into m2..");
        System.out.println("Sign Out m2..");
    }
}
