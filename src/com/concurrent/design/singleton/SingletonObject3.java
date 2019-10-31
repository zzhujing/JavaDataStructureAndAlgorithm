package com.concurrent.design.singleton;

/**
 * 懒汉式-double-check
 */
public class SingletonObject3 {

    private SingletonObject3() {

    }

    private static volatile SingletonObject3 instance;

    //使用double check优化性能，但是在SingletonObject3的初始化还没结束的时候执行了getInstance那么可能会导致空指针异常
    //因为可能还有初始化完毕instance对象就被其他的线程从堆中返回出去了。此时需要加上volatile关键字表明内存可见
    public static SingletonObject3 getInstance() {
        if (null == instance) {
            synchronized (SingletonObject3.class) {
                if (null == instance) {
                    instance = new SingletonObject3();
                }
            }
        }
        return instance;
    }
}