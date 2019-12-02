package com.concurrent.thread.create;

/**
 * @author : hujing
 * @date : 2019/10/25
 */
public class CreateThread3 {
    public static int counter = 0;

    public static void main(String[] args) {

        //创建Thread的时候指定其虚拟机栈的大小，按理来说虚拟机栈越小，相同JVM能创建的线程就越多
        //可以通过JVM参数-XSS10M来设置这个值的大小.
        new Thread(null, () -> {
            try {
                add(0);
            } catch (Error e) {
                e.printStackTrace(); //print stackOverFlow
                System.out.println(counter);
            }
        }, "Custom-Thread", 1 << 24).start();
    }

    public static void add(int n) {
        counter++;
        add(n + 1);
    }
}
