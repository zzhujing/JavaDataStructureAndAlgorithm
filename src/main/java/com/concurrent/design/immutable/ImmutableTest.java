package com.concurrent.design.immutable;


/**
 * 值对象： 八种基本类型，int a = 1; b = a;
 * 引用对象 ： 除了基本类型都是引用对象
 *      不可变Object:
 *          final 修饰 ， 初始化之后就不能更改其状态 ， 线程安全
 *      可变对象：
 *          可以改变状态 ， 线程不安全
 */
public class ImmutableTest {

    public static void main(String[] args) throws InterruptedException {
//        SyncObj obj = new SyncObj();
//        obj.setName("hujing");
//        long start = System.currentTimeMillis();
//        Thread t1 = new Thread(() -> {
//            for (int i = 0; i < 1_000_000; i++) {
//                System.out.println(obj.toString());
//            }
//        }, "t1");
//        Thread t2 = new Thread(() -> {
//            for (int i = 0; i < 1_000_000; i++) {
//                System.out.println(obj.toString());
//            }
//        }, "t2");
//        t1.start();
//        t2.start();
//        t1.join();
//        t2.join();
//
//        System.out.println((System.currentTimeMillis() - start) + "ms");
//    }

        //值对象
        char a = 'a';
        char tmp = a;
        tmp += 'b';
        System.out.println(a);

        //不可变引用对象
        String aa = "aa";
        String aaTmp = aa;
        aaTmp += "bb";
        System.out.println(aa);

        //可变
        StringBuilder s = new StringBuilder("aa");
        StringBuilder sbTmp = s;
        sbTmp.append("dd");
        System.out.println(s.toString());
    }


    final class ImmutableObj {
        private final String name;

        ImmutableObj(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            String result = name;
            return result;
        }
    }

    class SyncObj {
        private String name;

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public synchronized String toString() {
            return name;
        }
    }
}