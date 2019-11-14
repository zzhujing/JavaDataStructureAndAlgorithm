package com.concurrent.design.singleton;

/**
 * 懒汉式-Synchronized用法
 */
public class SingletonObject2 {

    private SingletonObject2() {
    }

    private static SingletonObject2 instance;

    //使用synchronized加锁，此时会有性能问题，因为后面获取只读的instance还需要获取锁
    public synchronized static SingletonObject2 getInstance() {
        if (null == instance) {
            instance = new SingletonObject2();
        }
        return instance;
    }
}
