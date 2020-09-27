package com.concurrent.thread.threadapi;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/10/25
 * {@link Thread#join()} api的用处 : 父线程会等待调用join()方法的线程执行一段时候后再执行,若不传时间参数为一直等待childThread运行完毕
 */
public class ThreadJoinDemo {
    public static void main(String[] args) throws InterruptedException {
//        mainThreadJoin();
//        orderExecThread2();
//        orderExecThreadWithCountDownLatch();

        ThreadService threadService = new ThreadService();
        threadService.exec(()-> {
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadService.shutdown(5000L);
    }

    static class ThreadService{


        private Thread execThread;
        private boolean flag = false;

        void exec(Runnable task) {
            execThread = new Thread(()->{
                try {
                    Thread t = new Thread(task);
                    t.setDaemon(true);
                    t.start();
                    t.join();
                    flag = true;
                } catch (InterruptedException e) {
                    System.out.println("interrupt...");
                }
            });
            execThread.start();
        }

        void shutdown(long timeout) {
            long cur = System.currentTimeMillis();
            while(!flag) {
                if (System.currentTimeMillis() - cur >= timeout) {
                    execThread.interrupt(); //打断
                    System.out.println("TimeOut..");
                    break;
                }
            }
        }
    }


    public static void orderExecThread1() throws InterruptedException {
        Thread a = new Thread(() -> System.out.println("a"));
        Thread b = new Thread(() -> System.out.println("b"));
        Thread c = new Thread(() -> System.out.println("c"));
        a.start();
        a.join(); //让方法所在线程wait(),当a线程terminal的时候会释放
        b.start();
        b.join();
        c.start();
        c.join();
    }

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    public static boolean flag1 = false;
    public static boolean flag2 = false;

    public static void orderExecThread2() throws InterruptedException {
        Thread a = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("a");
                flag1 = true;
                lock1.notify();
            }
        });
        Thread b = new Thread(() -> {
            synchronized (lock1) {
                try {
                    if (!flag1) {
                        lock1.wait();
                    }
                    synchronized (lock2) {
                        System.out.println("b");
                        flag2 = true;
                        lock2.notify();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        Thread c = new Thread(() -> {
            synchronized (lock2) {
                try {
                    if (!flag2) {
                        lock2.wait();
                    }
                    System.out.println("c");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();
        b.start();
        c.start();
    }

    public static final CountDownLatch l1 = new CountDownLatch(1);
    public static final CountDownLatch l2 = new CountDownLatch(1);

    public static void orderExecThreadWithCountDownLatch() throws InterruptedException {
        Thread a = new Thread(() -> {
            System.out.println("a");
            l1.countDown();
        });
        Thread b = new Thread(() -> {
            try {
                l1.await();
                System.out.println("b");
                l2.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread c = new Thread(() -> {
                try {
                    l2.await();
                    System.out.println("c");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        });
        a.start();
        b.start();
        c.start();
    }

    private static void mainThreadJoin() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            IntStream.range(1, 1000).forEach(System.out::println);
            System.out.println("child thread finish done.");
        });
        t1.start();
        t1.join(); //Main线程会等待t1结束之后再开始执行
        IntStream.range(1, 1000).forEach(System.out::println);
    }
}
