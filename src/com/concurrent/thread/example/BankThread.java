package com.concurrent.thread.example;

/**
 * @author : hujing
 * @date : 2019/10/25
 * 直接继承Thread会导致业务数据和执行单元和Thread混淆在一起，浪费资源, 如何将业务数据和执行单元抽离呢? Runnable
 */
public class BankThread {
    public static void main(String[] args) {
        new TicketWindowThread("NO:1柜台").start();
        new TicketWindowThread("NO:2柜台").start();
        new TicketWindowThread("NO:3柜台").start();
    }
}
