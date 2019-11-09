package com.concurrent.thread.lock;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author : hujing
 * @date : 2019/10/29
 * 自定义锁测试类
 */
public class CustomLockTest {

    private static final BooleanLock lock = new BooleanLock();

    public static void main(String[] args) throws InterruptedException {

        Stream.of("t1", "t2").forEach(name -> {
            new Thread(() -> {
                try {
                    lock.lock();
                    Optional.of(Thread.currentThread().getName() + "->开始工作").ifPresent(System.out::println);
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        lock.unlock();
                        Optional.of(Thread.currentThread().getName() + "->工作结束").ifPresent(System.out::println);
                    } catch (InterruptedException e) {
                        System.out.println("unlock fail");
                        e.printStackTrace();
                    }
                }
            }, name).start();
        });

        //此时如果使用Main线程去解锁，会出现问题
        lock.unlock();
    }
}
