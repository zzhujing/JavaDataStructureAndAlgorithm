package com.concurrent.classloader;

/**
 * @author : hujing
 * @date : 2019/11/6
 */
public class ClassLoaderTest {

    public static void main(String[] args) {
        ClassLoaderTest test = new ClassLoaderTest();
        test.variablePlusPLus();
    }

    public void variablePlusPLus() {
        int a = 10;
        a++;
        System.out.println(a);

        int b = 10;
        ++b;
        System.out.println(b);
    }
}
