package com.concurrent.volatiles;


/**
 * 测试volatile的作用程序
 * 根据Java的内存模型，由于主内存相对于高速的CPU来说处理速度相对较慢，所以处理数据的时候会在CPU和主存之间添加一道cache，每个线程都有自己的cpu cache
 * 所以在UPDATER 线程修改了自己的cache和主存上的数据之后，READER并不会接受到INITIAL被修改的信息 （是因为Java自作聪明的将reader线程改造只读取cpu cache）
 *
 * 1. volatile 内存可见，对缓存的操作立刻写入主内存
 * 2. volatile 设置的元素不会受到重排序的影响
 * 3. volatile 写操作会让其他CPU缓存失效
 *
 *
 * 使用场景：
 *  1. 对多个线程之间需要内存可见的数据进行标记
 *  2. 对又先后初始化顺序的属性进行指令重排拒绝
 */
public class VolatileTest1 {

    //volatile使用了CPU高速缓存一致性协议，会同其他CPU中的Cache过期
    private static volatile int INITIAL = 0;

    private static final int MAX = 5;

    public static void main(String[] args) {

        new Thread(() -> {
            int curCount = INITIAL;
            while (INITIAL < MAX) {
                //若INITIAL不使用volatile修饰，那么Java会将INITIAL在该线程中自动优化为到CPU cache中获取数据，而不会到主内存中获取
                if (curCount != INITIAL) {
                    System.out.printf("read curCount : %d\n", curCount);
                }
                curCount = INITIAL;
            }
        }, "READER").start();

        new Thread(() -> {
            int curCount = INITIAL;
            while (INITIAL < MAX) {
                System.out.printf("update curCount : %d\n", ++curCount);
                INITIAL = curCount;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "UPDATER").start();


    }
}
