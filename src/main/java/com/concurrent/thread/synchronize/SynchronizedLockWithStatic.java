package com.concurrent.thread.synchronize;


/**
 * 对于静态方法获取静态代码块来说，synchronized lock 为当前所在类的Class对象
 */
public class SynchronizedLockWithStatic {

    public static void main(String[] args) {
        new Thread(SynchronizedLockWithStatic::m1).start();
        new Thread(SynchronizedLockWithStatic::m2).start();
    }

    static {
        synchronized (SynchronizedLockWithStatic.class) {
            System.out.println("static block ");
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized static void m1() {
        try {
            System.out.println("m1");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized static void m2() {
        try {
            System.out.println("m2");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
