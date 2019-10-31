package com.concurrent.thread.threadpool;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 自定义简单线程池
<<<<<<< HEAD:src/com/concurrent/thread/threadpoll/SimpleThreadPool.java
 * 线程池的特点
 * 1. 初始化的时候会初始化特定数量线程
 * 2. 接受任务放置到任务队列
 * 3. 当任务超过一定数量值的时候有拒绝策略
 * 4. 动态队列
=======
 * 1. 启动即拥有默认数量工作线程
 * 2. 维护任务队列
 * 3. 队列满了的性能拒绝策略
 * 4. 动态增加/缩小工作线程数量
>>>>>>> c85fab7ef785995c692f317fe84b6b2a17bca596:src/com/concurrent/thread/threadpool/SimpleThreadPool.java
 */
public class SimpleThreadPool extends Thread {

    private static int seq;
<<<<<<< HEAD:src/com/concurrent/thread/threadpoll/SimpleThreadPool.java
    private final int size;
    private final int taskSize;
=======
    private int size;
    private volatile int taskSize;
>>>>>>> c85fab7ef785995c692f317fe84b6b2a17bca596:src/com/concurrent/thread/threadpool/SimpleThreadPool.java
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

    private int min;
    private int max;
    private int active;

    public SimpleThreadPool() {
        this(4, 8, 12, MAX_TASK_CAPACITY, DEFAULT_REJECT_POLICY);
    }

    public SimpleThreadPool(int min, int active, int max, int taskSize, RejectPolicy rejectPolicy) {
        this.min = min;
        this.max = max;
        this.active = active;
        this.taskSize = taskSize;
        this.rejectPolicy = rejectPolicy;
        init();
    }

    private void init() {
        //初始化对应数量线程去接受任务
        for (int i = 0; i < min; i++) {
            createThread();
        }
        //默认创建min数量的线程
        this.size = min;
        this.start();
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
            if (TASK_QUEUE.size() > taskSize)
                rejectPolicy.reject();
            TASK_QUEUE.addLast(task);
            TASK_QUEUE.notifyAll();
        }
    }

    public void shutdown() {
<<<<<<< HEAD:src/com/concurrent/thread/threadpoll/SimpleThreadPool.java
        //当任务队列还有任务的时候休眠等待任务结束
=======
        //当任务队列还有任务的时候休眠等待任务结束，也可以写一个shutdownNow方法，那么这里的策略就是直接关闭线程然后将没执行完的Runnable对象返回
>>>>>>> c85fab7ef785995c692f317fe84b6b2a17bca596:src/com/concurrent/thread/threadpool/SimpleThreadPool.java
        while (!TASK_QUEUE.isEmpty()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

<<<<<<< HEAD:src/com/concurrent/thread/threadpoll/SimpleThreadPool.java
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
=======
        synchronized (THREAD_QUEUE) {
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
>>>>>>> c85fab7ef785995c692f317fe84b6b2a17bca596:src/com/concurrent/thread/threadpool/SimpleThreadPool.java
                    }
                }
            }
        }
    }

    //当前线程池也继承了Thread类，并且在run()中有动态改变工作线程数的逻辑
    @Override
    public void run() {
        while (!destroy) {

            System.out.printf("min:%d,active:%d,max:%d,size:%d,TaskQueue：%d", min, active, max, size, TASK_QUEUE.size());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (TASK_QUEUE.size() > min && size < active) {
                for (int i = min; i < active; i++) {
                    createThread();
                }
                System.out.println("Thread size increment to active.");
                this.size = active;
            } else if (TASK_QUEUE.size() > active && size < max) {
                for (int i = active; i < max; i++) {
                    createThread();
                }
                System.out.println("Thread size increment to max.");
                this.size = max;
            }

            //当暂时没有任务的时候，并且大于active线程数量的时候release掉部分线程
            if (TASK_QUEUE.isEmpty() && this.size > active) {
                synchronized (THREAD_QUEUE) {
                    //移除指定数量的线程
                    Iterator<Worker> iterator = THREAD_QUEUE.iterator();
                    while (iterator.hasNext() && this.size != active) {
                        Worker worker = iterator.next();
                        if (worker.state == TaskState.BLOCKED) {
                            worker.interrupt();
                            worker.close();
                            iterator.remove();
                            size--;
                        } else {
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                System.out.println("Thread size decrement to active..");
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

    //线程状态枚举
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
<<<<<<< HEAD:src/com/concurrent/thread/threadpoll/SimpleThreadPool.java
                            //这里的目的是要退出到状态判断label
=======
                            System.out.println("Closed..");
>>>>>>> c85fab7ef785995c692f317fe84b6b2a17bca596:src/com/concurrent/thread/threadpool/SimpleThreadPool.java
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
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("the no -> " + i + "  task finished.——> " + Thread.currentThread());
                    });
                });
        Thread.sleep(20_000);
        threadPool.shutdown();
    }
}
<<<<<<< HEAD:src/com/concurrent/thread/threadpoll/SimpleThreadPool.java

=======
>>>>>>> c85fab7ef785995c692f317fe84b6b2a17bca596:src/com/concurrent/thread/threadpool/SimpleThreadPool.java
