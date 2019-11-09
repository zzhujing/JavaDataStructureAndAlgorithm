package com.concurrent.design.singleton;


/**
 * 单例模式之饿汉式
 */
public class SingletonObject1 {

    private static final SingletonObject1 instance = new SingletonObject1();

    private SingletonObject1() {
    }
    //can't lazy load.
    public static SingletonObject1 getInstance() {
        return instance;
    }
}
