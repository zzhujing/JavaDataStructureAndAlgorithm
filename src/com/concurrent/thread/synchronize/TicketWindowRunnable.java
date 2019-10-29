package com.concurrent.thread.synchronize;

public class TicketWindowRunnable implements Runnable {

    private static final int MAX_NO = 5000;
    private int curNo = 1;
    private static final Object MONITOR = new Object();
    @Override
    public void run() {
        //可以使用javap命令来查看字节码命令，并且在运行的时候可以通过jconsole,jstack来查看线程的运行情况
        while (true) {
            synchronized (MONITOR) {
                if (curNo > MAX_NO) break;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + " : 请" + (curNo++) + "号顾客来办理业务！！！");
            }
        }
    }
}
