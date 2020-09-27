package com.concurrent.thread.threadapi;

/**
 * @author : hujing
 * @date : 2019/10/29
 * 抓取Thread中未捕获的异常
 */
public class CaughtThreadException {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                m1();
                int i = 1 / 0;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        //设置未捕获异常的处理器
        t.setUncaughtExceptionHandler(
                (thread, e) -> {
                    System.out.println(thread);
                    System.out.println(e);
                });
        t.start();


        //遍历线程堆栈调用信息
//        Arrays.stream(t.getStackTrace()).forEach(e -> {
//            System.out.println(e.getClassName() + ": " + e.getClassName() + ": " + e.getLineNumber());
//        });
    }

    private static void m1() {
    }
}
