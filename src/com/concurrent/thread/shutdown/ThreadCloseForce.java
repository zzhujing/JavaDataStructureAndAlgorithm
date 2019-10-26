package com.concurrent.thread.shutdown;

/**
 * 针对使用中断的优雅关闭的方式，它的缺点是需要自身能够判断是否已经打断，而面对那种一个线程可能处理耗时任务无法自己判断
 *
 * 使用守护线程去执行任务，然后可以控制主线程来控制任务
 */
public class ThreadCloseForce {
    public static void main(String[] args) {
        ThreadService service = new ThreadService();
        long start = System.currentTimeMillis();
        service.execute(() -> {
            //模拟耗时任务
            while (true) {
            }
        });
        service.shutdown(3000);
        long end = System.currentTimeMillis();
        System.out.println("一共耗时 -> " + (end - start) + "ms");
    }

    private static class ThreadService {

        //执行任务线程
        private Thread executeThread;

        //该标志用来显示指明Task任务已经执行完毕了
        private boolean finished = false;

        void execute(Runnable task) {
            executeThread = new Thread(() -> {
                //使用守护线程来执行Task
                Thread t = new Thread(task);
                t.setDaemon(true);
                t.start();
                try {
                    //使用join来阻塞executeThread
                    t.join();
                    finished = true;
                } catch (InterruptedException e) {
                    System.out.println(executeThread.getName() + " = > interrupt");
                }
            });
            executeThread.start();
        }
        /**
         *  关闭方法
         * @param timeoutMillis  超时时间
         */
        void shutdown(long timeoutMillis) {
            long start = System.currentTimeMillis();
            while (!finished) {
                //如果没有成功，那么我们需要判断是否超时
                if ((System.currentTimeMillis() - start) >= timeoutMillis) {
                    //超时
                    System.out.println("任务超时。。。");

                    //打断阻塞的执行线程从而关闭Task
                    executeThread.interrupt();
                    return;
                }
            }
            finished = false;
        }
    }
}
