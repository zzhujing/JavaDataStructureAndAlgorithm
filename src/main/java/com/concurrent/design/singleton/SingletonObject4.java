package com.concurrent.design.singleton;

/**
 * 静态内部类方式
 */
public class SingletonObject4 {

    private SingletonObject4() {
    }

    /**
     * 静态内部类只有在被调用的时候才会加载一次
     */
    private static class InstanceHolder {
        private static final SingletonObject4 instance = new SingletonObject4();
    }

    public static SingletonObject4 getInstance() {
        return InstanceHolder.instance;
    }
}
