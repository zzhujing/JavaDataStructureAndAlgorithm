package com.concurrent.thread.threadapi;

/**
 * @author : hujing
 * @date : 2019/10/25
 * {@link Thread#interrupt()} 方法演示 : 该方法用来打断sleep(),wait(),join()等阻塞方法的执行,打断后这些方法后抛出{@link InterruptedException}
 */
public class ThreadInterruptDemo {

    private static final Object MONITOR = new Object();

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Thread t = new Thread(() -> {
            while (true) {
                }
        });

        //在join阻塞Main线程之后使用第二个线程中断join
        Thread t2 = new Thread(()->{
            try {
                Thread.sleep(1000L);
                System.out.println(mainThread.isInterrupted());
                mainThread.interrupt();
                System.out.println(mainThread.isInterrupted());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();
        t2.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            System.out.println("join interrupt");
            e.printStackTrace();
        }
        System.out.println("Main Thread Done..");
    }
}
