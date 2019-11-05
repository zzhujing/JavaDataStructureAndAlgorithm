package com.concurrent.classloader;

import java.lang.reflect.InvocationTargetException;

public class TestCustomClassLoader {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        CustomClassLoader customClassLoader1 = new CustomClassLoader();
        CustomClassLoader customClassLoader2 = new CustomClassLoader();
        customClassLoader2.setDir("/Users/hujing/dd");
        Class<?> clazz1 = customClassLoader1.loadClass("com.concurrent.volatiles.VolatileTest1");
        Class<?> clazz2 = customClassLoader2.loadClass("com.concurrent.volatiles.VolatileTest1");
        System.out.println(clazz1.hashCode());
        System.out.println(clazz2.hashCode());
    }
}
