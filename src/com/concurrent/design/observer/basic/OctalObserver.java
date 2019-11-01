package com.concurrent.design.observer.basic;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public class OctalObserver extends Observer {

    public OctalObserver(Subject subject) {
        super(subject);
    }
    @Override
    protected void update() {
        System.out.println("OctalObserver  -> " + Integer.toOctalString(subject.getState()));
    }
}
