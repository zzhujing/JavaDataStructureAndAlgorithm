package com.concurrent.chapter3;

import java.util.Arrays;

/**
 * @author : hujing
 * @date : 2019/10/25
 * Thread 的几个构造方法
 *
 * 不传名称的话则默认为 ：　Thread-(0~n)
 * 不传Runnable且不重写run()方法的话 , 将不会执行run()
 * ThreadGroup不传递默认为父线程的线程组名称
 * stackSize -> 可以设置初始化该Thread时候的虚拟机栈大小
 */
public class CreateThread {
    public static void main(String[] args) {

        Thread t = new Thread(()->{
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        System.out.println(t.getThreadGroup().getName());
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();

        //获取到Main线程组中所有的线程个数
        int activeCount = threadGroup.activeCount();

        //获取所有的线程
        Thread[] list = new Thread[activeCount];
        threadGroup.enumerate(list);
        System.out.println(Arrays.toString(list));
    }
}
