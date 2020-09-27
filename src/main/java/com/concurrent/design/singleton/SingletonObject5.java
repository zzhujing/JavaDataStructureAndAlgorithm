package com.concurrent.design.singleton;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Field;
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
        //枚举构造方法模式是私有的。那么只有在枚举实例话的时候才会被调用一次
        InstanceHolder() {
            instance = new SingletonObject5();
        }

        public static SingletonObject5 getInstance(){
            return INSTANCE.instance;
        }
    }
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        IntStream.rangeClosed(1, 100)
                .forEach(i -> new Thread(String.valueOf(i)) {
                    @Override
                    public void run() {
                        System.out.println(SingletonObject5.InstanceHolder.getInstance());
                    }
                }.start());
    }
}
