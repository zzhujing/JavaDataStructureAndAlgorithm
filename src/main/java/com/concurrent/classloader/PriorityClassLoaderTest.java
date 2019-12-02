package com.concurrent.classloader;

import com.concurrent.volatiles.VolatileTest1;

public class PriorityClassLoaderTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        PriorityChildClassLoader priorityChildClassLoader = new PriorityChildClassLoader();
        final Class<?> clazz = priorityChildClassLoader.loadClass("com.concurrent.volatiles.VolatileTest1");
        System.out.println(clazz.getClassLoader());
//        这里因为类加载的运行时包，由子类加载器加载的 VolatileTest1 是无法强转为 AppClassLoader的 ， 因为命名空间也不一样
        final VolatileTest1 volatileTest1 = (VolatileTest1) clazz.newInstance();
    }
}
