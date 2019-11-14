package com.concurrent.classloader;

import java.lang.reflect.Method;

public class DecryptClassLoaderTest {
    public static void main(String[] args) throws Exception {
        DecryptClassLoader decryptClassLoader = new DecryptClassLoader();
        final Class<?> clazz = decryptClassLoader.loadClass("com.concurrent.classloader.MyObject");
        System.out.println(clazz);
        final Method hello = clazz.getMethod("hello");
        hello.invoke(clazz.newInstance());
    }
}
