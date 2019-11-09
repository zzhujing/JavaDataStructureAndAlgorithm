package com.concurrent.thread.example;

/**
 * @author : hujing
 * @date : 2019/10/25
 */
public class BankRunnable {
    public static void main(String[] args) {
        final TicketWindowRunnable ticketWindowRunnable = new TicketWindowRunnable();
        new Thread(ticketWindowRunnable,"一号窗口").start();
        new Thread(ticketWindowRunnable,"二号窗口").start();
        new Thread(ticketWindowRunnable,"三号窗口").start();
    }
}
