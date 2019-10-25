package com.concurrent.chapter1;


/**
 * 1. Java应用启动的时候JVM会创建Main非守护线程
 * 2. 实现一个线程 ： 创建一个Thread实例，重写run()，然后调用start()启动
 * 3. 调用一个线程的start(), 此时至少有两个线程， 一个是调用你的线程，一个是执行run()的线程
 * 4. 线程的状态有 : new , runnable , running , blocked , terminated
 * 5. 为什么重写的是run()，然而启动的时候是调用start() ?
 *      因为Thread类设计用的是模板方法模式，在start()中调用run()的前后会执行一些增强，比如调用start0()启动线程
 * new Thread()  : null -> new
 * start() :  new -> runnable
 * Dispatcher获取到CPU的调度权 ： runnable -> running
 * Sleep或者其他阻塞 ： running -> blocked -> runnable(阻塞结束需要重新回到runnable等待CPU调度)
 * 执行完毕，获取程序断开 ： running , blocked , runnable 都可能直接-> terminated
 */
public class ThreadDemo{
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                super.run();
            }
        }.start(); //这里调用start()才启动线程是用了模板设计模式，在start()方法中会默认调用native方法的start0来启动线程，然后run()为我们的业务执行
    }
}


class TemplateMethod{

    public final void print(String message) {
        System.out.println("------------");  //Thread会在这里调用start0来启动线程
        wrapPrint(message);                  //相当于run()
        System.out.println("------------");
    }

    protected void wrapPrint(String message){
    }
    public static void main(String[] args) {

        //模拟Thread的实现
         new TemplateMethod() {
            @Override
            protected void wrapPrint(String message) {
                System.out.println(message);
            }
        }.print("Hello,TemplateMethod");
    }
}