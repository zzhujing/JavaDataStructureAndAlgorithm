package com.concurrent.thread.threadpoll;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 自定义简单线程池
 * 线程池的特点
 * 1. 初始化的时候会初始化特定数量线程
 * 2. 接受任务放置到任务队列
 * 3. 当任务超过一定数量值的时候有拒绝策略
 * 4. 动态队列
 */
public class SimpleThreadPool {

    private static final int DEFAULT_SIZE = 10;
    private static int seq;
    private final int size;
    private final int taskSize;
    private static final int MAX_TASK_CAPACITY = 2000;
    private static final LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();
    private static final List<Worker> THREAD_QUEUE = new ArrayList<>();
    private static final ThreadGroup GROUP = new ThreadGroup("POOL_GROUP");
    private static final String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";
    private volatile boolean destroy = false;
    private static final RejectPolicy DEFAULT_REJECT_POLICY = () -> {
        throw new RejectException("reject");
    };
    private final RejectPolicy rejectPolicy;

    public SimpleThreadPool() {
        this(DEFAULT_SIZE, MAX_TASK_CAPACITY, DEFAULT_REJECT_POLICY);
    }

    public SimpleThreadPool(int size, int taskSize, RejectPolicy rejectPolicy) {
        this.size = size;
        this.taskSize = taskSize;
        this.rejectPolicy = rejectPolicy;
        init();
    }

    private void init() {
        //初始化对应数量线程去接受任务
        for (int i = 0; i < size; i++) {
            createThread();
        }
    }

    private void createThread() {
        Worker worker = new Worker(GROUP, THREAD_PREFIX + (seq++));
        worker.start();
        THREAD_QUEUE.add(worker);
    }

    public void submit(Runnable task) {

        if (destroy) {
            throw new IllegalStateException("The Thread Pool Already Destroy");
        }
        synchronized (TASK_QUEUE) {
            //这里添加任务的时候需要考虑拒绝策略。
            System.out.println(TASK_QUEUE.size());
            if (TASK_QUEUE.size() > taskSize)
                rejectPolicy.reject();
            TASK_QUEUE.addLast(task);
            TASK_QUEUE.notifyAll();
        }
    }

    public void shutdown() {
        //当任务队列还有任务的时候休眠等待任务结束
        while (!TASK_QUEUE.isEmpty()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //获取当前线程数
        int curThreadCount = THREAD_QUEUE.size();
        while (curThreadCount > 0) {
            for (Worker t : THREAD_QUEUE) {
                //如果当前线程处于BLOCKED状态说明正在wait()
                if (t.state == TaskState.BLOCKED) {
                    //打断wait()让线程完成
                    t.interrupt();
                    //修改线程状态值
                    t.close();
                    curThreadCount--;
                    this.destroy = true;
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public int getSize() {
        return size;
    }

    public int getTaskSize() {
        return taskSize;
    }

    public boolean isDestroy() {
        return this.destroy;
    }


    private static class RejectException extends RuntimeException {
        public RejectException(String message) {
            super(message);
        }
    }


    //拒绝策略
    public interface RejectPolicy {
        void reject() throws RejectException;
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

    /**
     * 工作线程
     */
    private static class Worker extends Thread {

        private volatile TaskState state;

        public Worker(ThreadGroup group, String name) {
            super(group, name);
            this.state = TaskState.FREE;
        }

        @Override
        public void run() {
            OUTER:
            while (this.state != TaskState.DEAD) {
                //操作任务队列需要加锁
                Runnable runnable;
                synchronized (TASK_QUEUE) {
                    if (TASK_QUEUE.isEmpty()) {
                        try {
                            this.state = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            //这里的目的是要退出到状态判断label
                            break OUTER;
                        }
                    }
                    //从队列中获取一个任务
                    runnable = TASK_QUEUE.removeFirst();
                    System.out.println("remove -> " + TASK_QUEUE.size());
                }
                if (runnable != null) {
                    this.state = TaskState.RUNNING;
                    runnable.run();
                    this.state = TaskState.FREE;
                }
            }
        }

        public void close() {
            this.state = TaskState.DEAD;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool threadPool = new SimpleThreadPool(7, 10, DEFAULT_REJECT_POLICY);
        IntStream.rangeClosed(0, 40)
                .forEach(i -> {
                    threadPool.submit(() -> {
                        System.out.println("the no -> " + i + " task begin. ——> " + Thread.currentThread());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("the no -> " + i + "  task finished.——> " + Thread.currentThread());
                    });
                });
        Thread.sleep(10_000);
        threadPool.shutdown();
    }
}

