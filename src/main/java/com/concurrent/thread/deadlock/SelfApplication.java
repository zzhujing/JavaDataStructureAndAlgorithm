package com.concurrent.thread.deadlock;


/**
 *  自己的同步业务程序,去调用另外一个带锁的程序
 */
public class SelfApplication {

    private OtherApplication otherApplication;

    public SelfApplication(OtherApplication otherApplication) {
        this.otherApplication = otherApplication;
    }

    private final Object lock = new Object();

    public void m1(){
        synchronized (lock) {
            System.out.println("m1");
            //调用OtherApplication的业务
            otherApplication.o1();
        }
    }
    public void m2() {
        synchronized (lock) {
            System.out.println("m2");
        }
    }
}
