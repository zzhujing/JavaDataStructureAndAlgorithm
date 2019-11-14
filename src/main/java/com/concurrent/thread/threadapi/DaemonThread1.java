package com.concurrent.thread.threadapi;

/**
 * @author : hujing
 * @date : 2019/10/25
 * 守护线程 ： 跟随着主线程的退出一起退出
 * 使用场景　：　比如主线程是一个网络连接,　为了验证状态需要使用另外的线程发送心跳包　,此时主线程退出以后就没必要心跳了
 */
public class DaemonThread1 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            String curThreadName = Thread.currentThread().getName();
            System.out.println(curThreadName +": running");
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(curThreadName + ": terminated");
        });
        //设置为守护线程
        t.setDaemon(true);
        t.start();
        System.out.println("MainThread Terminated");
        //此时Main退出之后守护线程也会随之退出
    }
}
