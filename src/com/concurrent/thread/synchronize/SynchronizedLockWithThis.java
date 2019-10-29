package com.concurrent.thread.synchronize;


/**
 * 对于实例方法来说，其synchronize lock为当前this对象
 */
public class SynchronizedLockWithThis {

    public static void main(String[] args) {
        SynchronizedLockWithThis synchronizedLockWithThis = new SynchronizedLockWithThis();
        new Thread(synchronizedLockWithThis::m1).start();
        new Thread(synchronizedLockWithThis::m2).start();
    }


    public synchronized void m1() {
        try {
            System.out.println("m1");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public synchronized void m2() {
        try {
            System.out.println("m2");
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
