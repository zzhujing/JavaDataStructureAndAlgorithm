package com.concurrent.thread.shutdown;

/**
 * 终止线程执行的方式:
 * 1. 定义一个标志符号{@link Worker#flag}，需要中断的时候修改标志符号{@link Worker#shutdown()}
 * 2. 使用{@link Thread#interrupt()} 打断一些阻塞方法并在catch到的{@link InterruptedException} 中退出线程
 */
public class ThreadCloseGracefulWithFlag {

    public static void main(String[] args) {
        Worker worker = new Worker();
        worker.start();
        try {
            //等待三秒，没执行完就打断
            Thread.sleep(3_000);
            worker.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static class Worker extends Thread{

        private volatile boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                //do some spend time thing
                try {
                    System.out.println("begin work");
                    Thread.sleep(10_000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        private void shutdown(){
            flag = false;
        }
    }
}
