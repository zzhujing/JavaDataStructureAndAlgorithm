package com.concurrent.thread.threadapi;

import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/10/25
 * {@link Thread#join()} api的用处 : 父线程会等待调用join()方法的线程执行一段时候后再执行,若不传时间参数为一直等待childThread运行完毕
 */
public class ThreadJoinDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 1000).forEach(System.out::println);
            System.out.println("child thread finish done.");
        });
        t1.start();
        t1.join(); //Main线程会等待t1结束之后再开始执行
        IntStream.range(1, 1000).forEach(System.out::println);
    }
}
