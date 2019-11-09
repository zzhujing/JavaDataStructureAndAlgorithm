package com.concurrent.thread.synchronize;


public class SynchronizedTicketWindowDemo {


    public static void main(String[] args) {

        final TicketWindowRunnable ticketWindowTask = new TicketWindowRunnable();

        new Thread(ticketWindowTask, "1号窗口").start();
        new Thread(ticketWindowTask, "2号窗口").start();
        new Thread(ticketWindowTask, "3号窗口").start();
    }
}
