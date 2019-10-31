package com.concurrent.design.singleton;

import java.util.stream.IntStream;

/**
 * 枚举方式单例
 */
public class SingletonObject5 {

    private SingletonObject5() {
    }

    //枚举其实也是个static的类，在调用的时候才会完成初始化
    private enum InstanceHolder {
        INSTANCE;

        private final SingletonObject5 instance;

        InstanceHolder() {
            instance = new SingletonObject5();
        }
    }

    public static SingletonObject5 getInstance() {
        return InstanceHolder.INSTANCE.instance;
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(1, 100)
                .forEach(i -> new Thread(String.valueOf(i)) {
                    @Override
                    public void run() {
                        System.out.println(SingletonObject5.getInstance());
                    }
                }.start());
    }

}
