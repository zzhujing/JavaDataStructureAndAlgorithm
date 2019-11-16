package com.concurrent.juc.utils;

import java.util.concurrent.Exchanger;

/**
 * Exchanger 用来在两线程之间传递数据 ，
 * 1. 使用exchanger(V v) , 在交换的时候会阻塞住。可以设置超时时间
 * 2. 传递的为真实引用地址，会有多线程并发问题
 * 3. 可以多次传递exchange()
 */
public class ExchangerTest {

    public static void main(String[] args) {
        final Exchanger<String> exchanger = new Exchanger<>();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " begin work.");
            try {
                //这边直接使用Exchanger.exchange()方法交换数据，且交换之后的对象不是拷贝值是源对象， 所有在两线程直接传递数据会有多线程安全问题
                System.out.println(Thread.currentThread().getName() + ":" + exchanger.exchange("message from Thread -A"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished work.");
        }, "A").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " begin work.");
            try {
                System.out.println(Thread.currentThread().getName() + ":" + exchanger.exchange("message from Thread -B"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finished work.");
        }, "B").start();

        System.out.println("Main Thread finished!");
    }
}
