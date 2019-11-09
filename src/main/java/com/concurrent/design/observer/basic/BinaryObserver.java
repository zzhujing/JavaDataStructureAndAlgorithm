package com.concurrent.design.observer.basic;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public class BinaryObserver extends Observer {
    public BinaryObserver(Subject subject) {
        super(subject);
    }
    @Override
    protected void update() {
            System.out.println("BinaryObserver -> " + Integer.toBinaryString(subject.getState()));
    }
}
