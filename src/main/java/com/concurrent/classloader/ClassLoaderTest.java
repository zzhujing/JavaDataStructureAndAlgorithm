package com.concurrent.classloader;

/**
 * @author : hujing
 * @date : 2019/11/6
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        System.out.println(ClassLoader.getSystemClassLoader() == Thread.currentThread().getContextClassLoader());
    }
}
