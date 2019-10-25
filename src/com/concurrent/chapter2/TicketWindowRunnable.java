package com.concurrent.chapter2;

/**
 * @author : hujing
 * @date : 2019/10/25
 */
public class TicketWindowRunnable implements Runnable {

    private static final int MAX_NO = 50;

    private int curNO = 1;

    @Override
    public void run() {
        while (curNO <= MAX_NO) {
            System.out.println(Thread.currentThread().getName() + " : " + (curNO++) + ",请过来办理业务！！");
        }
    }
}
