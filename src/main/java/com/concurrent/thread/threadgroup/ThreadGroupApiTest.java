package com.concurrent.thread.threadgroup;

/**
 * @author : hujing
 * @date : 2019/10/29
 */
public class ThreadGroupApiTest {
    public static void main(String[] args) throws InterruptedException {


        ThreadGroup tg1 = new ThreadGroup("tg1");

        Thread t = new Thread(tg1, () -> {
            System.out.println(Thread.currentThread().getThreadGroup().getName());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getThreadGroup().activeCount());
            System.out.println(Thread.currentThread().getThreadGroup().activeGroupCount());
        }, "t1");
        t.start();
        ThreadGroup tg2 = new ThreadGroup(tg1, "tg2");
        System.out.println(tg2.getParent().getName());

//        Thread[] threadList = new Thread[10];
//        Thread.currentThread().getThreadGroup().enumerate(threadList, true);
//        Stream.of(threadList).forEach(System.out::println);
//        System.out.println(tg1.isDestroyed()); //判断是否最后一个线程已经执行完毕了，执行完毕则返回true
//        tg1.destroy(); //关闭ThreadGroup , 若还有线程存活那么会抛出IllegalThreadStateException
//        System.out.println(tg1.isDestroyed());
    }
}
