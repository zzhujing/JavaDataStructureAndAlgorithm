package com.concurrent.chapter4;

/**
 * @author : hujing
 * @date : 2019/10/25
 * 常见ThreadAPI
 */
public class ThreadAPIDemo1 {
    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getPriority());
        System.out.println(thread.getId());
        System.out.println(thread.getName());
        System.out.println(thread.getThreadGroup());
        System.out.println(thread.getState().name());
    }
}
