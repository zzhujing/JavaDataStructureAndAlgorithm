package com.concurrent.design.observer.basic;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public class ObserverClient {
    public static void main(String[] args) {
        Subject subject = new Subject();
        new BinaryObserver(subject);
        new OctalObserver(subject);
        subject.setState(10);
        System.out.println("+=====================+");
        subject.setState(10);
        System.out.println("+=====================+");
        subject.setState(12);
    }
}
