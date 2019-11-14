package com.concurrent.thread.shutdown;

/**
 * interrupt方式打断线程运行，用于在run()方法执行过程中能主动调用判断是否被打断的场景
 */
public class ThreadCloseGracefulWithInterrupt {
    public static void main(String[] args) {
        ThreadCloseGracefulWithInterrupt.Worker worker = new ThreadCloseGracefulWithInterrupt.Worker();
        worker.start();
        try {
            //等待三秒，没执行完就打断
            Thread.sleep(3_000);
            worker.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private static class Worker extends Thread{
        @Override
        public void run() {
            while (true) {
                //如果被打断就退出线程
                if (isInterrupted()) {
                    break;
                }
            }
        }
    }
}
