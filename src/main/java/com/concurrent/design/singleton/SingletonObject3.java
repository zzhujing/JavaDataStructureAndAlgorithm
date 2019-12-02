package com.concurrent.design.singleton;

/**
 * 懒汉式-double-check
 */
public class SingletonObject3 {

    //可能发生指令重排导致在初始化SingletonObject3的时候，这些对象还未初始化
    //private Object obj1;
    //private Object obj2;

    private SingletonObject3() {
        //init obj1 obj2 spend much time
    }

    private static volatile SingletonObject3 instance;

    //使用double check优化性能，但是在SingletonObject3的初始化还没结束的时候执行了getInstance那么可能会导致空指针异常
    //因为可能还有初始化完毕instance对象就被其他的线程从堆中返回出去了
    // instance = new SingletonObject3()
    //  -> 1. 获取堆中的SingleObject3的内存地址
    //  -> 2. 初始化
    //  -> 3. 将引用指向instance
    //正常顺序为 1->2->3 , 但是可能发生指令重排序 1->3->2导致没初始化完毕就将堆中对象引用指向instance，这样后面线程获取到就会造成空指针
    // 此时需要加上volatile关键字表明内存可见
    public static SingletonObject3 getInstance() {
        if (null == instance) {
            synchronized (SingletonObject3.class) {
                if (null == instance) {
                    instance = new SingletonObject3(); //可能造成指令重排，结果就是未初始化完毕就返回引用对象。
                }
            }
        }
        return instance;
    }
}