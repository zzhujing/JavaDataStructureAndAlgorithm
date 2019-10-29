package com.concurrent.thread.communicate;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 模拟限制线程数量去处理抓取业务
 */
public class CaptureService {

    //控制线程数量容器，Control无特殊含义
    private static final LinkedList<Control> CONTROLS = new LinkedList<>();
    private static final int MAX_CONTROL = 5;

    public static void main(String[] args) {

        List<Thread> worker = new ArrayList<>();


        //启动10个线程
        Stream.of("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M0")
                .map(CaptureService::createThread)
                .forEach(t -> {
                    t.start();
                    worker.add(t);
                });

        //join等待所有子线程执行完毕
        worker.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    public static Thread createThread(String name) {
        return new Thread(() -> {
            System.out.println(name + " -> 开始工作");
            synchronized (CONTROLS) {

                //最多使用五个线程去处理业务
                while (CONTROLS.size() - 1 > MAX_CONTROL) {
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                CONTROLS.addLast(new Control());
            }
            System.out.println(name + " -> 正在工作");
            try {
                //模拟工作耗时
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (CONTROLS) {
                System.out.println("工作完毕");
                //移除当前任务
                CONTROLS.removeFirst();
                //唤醒其他等待线程
                CONTROLS.notifyAll();
            }
        }, name);
    }

    private static class Control {

    }
}
