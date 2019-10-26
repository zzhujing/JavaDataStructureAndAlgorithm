package com.concurrent.thread.create;

/**
 * @author : hujing
 * @date : 2019/10/25
 * 模拟无限创建Thread
 */
public class CreateThread2 {

    private static int counter = 0;

    public static void main(String[] args) {
        //会死机
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            new Thread(() -> {
                counter++;
                byte[] arr = new byte[1024 * 1024 * 1024];
                while (true) {
                }
            }).start();
        }
        System.out.println(counter);
    }
}
