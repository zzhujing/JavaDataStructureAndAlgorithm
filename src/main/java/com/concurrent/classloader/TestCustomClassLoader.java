package com.concurrent.classloader;

import java.lang.reflect.InvocationTargetException;

/**
 * 不同类加载器实例加载的字节码不一样。
 * ClassLoader不同实例有运行时差异。
 */
public class TestCustomClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        PriorityChildClassLoader customClassLoader1 = new PriorityChildClassLoader();
        PriorityChildClassLoader customClassLoader2 = new PriorityChildClassLoader();
//        customClassLoader2.setDir("/Users/hujing/dd");
        Class<?> clazz1 = customClassLoader1.loadClass("com.concurrent.volatiles.VolatileTest1");
        Class<?> clazz2 = customClassLoader2.loadClass("com.concurrent.volatiles.VolatileTest1");
        System.out.println(clazz1.hashCode()); //1950409828
        System.out.println(clazz2.hashCode()); //1229416514
    }
}
