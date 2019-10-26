package com.concurrent.thread.example;

/**
 * @author : hujing
 * @date : 2019/10/25
 * 叫号窗口抽象
 */
public class TicketWindowThread extends Thread {

    private final String name;

    private static final int MAX_NO = 50;

    //申明为static变量保证多线程调用时内存独一份
    private static int curNo = 1;

    public TicketWindowThread(String name) {
        this.name = name;
    }

    @Override
    public void run() {

        while (curNo <= MAX_NO) {
            System.out.println(name + ": " + (curNo++) + "号来办理业务！！");
        }
    }
}
