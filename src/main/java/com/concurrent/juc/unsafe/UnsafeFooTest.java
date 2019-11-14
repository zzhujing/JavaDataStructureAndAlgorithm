package com.concurrent.juc.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 使用Unsafe的一些有意思的APi
 */
public class UnsafeFooTest {
    public static void main(String[] args) throws InstantiationException, NoSuchFieldException {
        //1. Unsafe能跳过初始化来使用类
        final Unsafe unsafe = UnsafeTest.getUnsafe();
        final Simple s = (Simple) unsafe.allocateInstance(Simple.class);
        System.out.println(s.get()); // 0
        //2. Unsafe加载.class文件
//        unsafe.defineClass()
        //3. 动态修改属性的值
        final Simple work = new Simple();
        work.work();
        Field f = Simple.class.getDeclaredField("i");
        unsafe.putInt(work, unsafe.objectFieldOffset(f), 43);
        work.work();
    }

    static class Simple {
        int i = 0;

        public Simple() {
            this.i = 1;
            System.out.println("init");
        }

        public void work() {
            if (i == 43) {
                System.out.println("i'm working ..");
            }
        }

        public int get() {
            return i;
        }
    }
}
