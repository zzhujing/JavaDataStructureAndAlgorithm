package com.concurrent.thread.deadlock;

/**
 * 另一个业务程序
 */
public class OtherApplication {

    private SelfApplication selfApplication;

    public void setSelfApplication(SelfApplication selfApplication) {
        this.selfApplication = selfApplication;
    }

    private final Object lock = new Object();

    public void o1() {
        synchronized (lock) {
            System.out.println("o1");
        }
    }

    public void o2() {
        synchronized (lock) {
            System.out.println("o2");
            //调用SelfApplication的业务
            selfApplication.m2();
        }
    }
}
