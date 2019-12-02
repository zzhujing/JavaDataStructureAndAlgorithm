package com.concurrent;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class SuspensionPattern {

    private static final MyLinkedBlockingQueue<String> queue = new MyLinkedBlockingQueue<>();
    private static volatile boolean close = false;
    private static final int MAX = 100;

    public static void main(String[] args) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        IntStream.rangeClosed(1, 5).boxed().forEach(a ->
                new Thread(() -> {
                    int per;
                    while ((per = counter.incrementAndGet()) < MAX) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                            queue.put("Hello->" + per);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "Client").start()
        );

        IntStream.rangeClosed(1, 5).boxed().forEach(a ->
                new Thread(() -> {
                    while (!close) {
                        try {
                            System.out.println(queue.take());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, "SERVER").start()
        );

        TimeUnit.SECONDS.sleep(10);
        close();
    }

    public static void close() {
        close = true;
    }


    static class MyLinkedBlockingQueue<T> {
        private final LinkedList<T> list = new LinkedList<>();
        private static final int capacity = 100;
        private final Lock lock = new ReentrantLock();
        private final Condition isEmpty = lock.newCondition();
        private final Condition isFull = lock.newCondition();


        public void put(T t) {
            try {
                lock.lock();
                while (list.size() >= capacity) {
                    isFull.await();
                }
                list.addLast(t);
                isEmpty.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


        public T take() throws InterruptedException {
            try {
                lock.lock();
                while (list.isEmpty()) {
                    isEmpty.await();
                }
                final T result = list.removeFirst();
                isFull.signalAll();
                return result;
            } finally {
                lock.unlock();
            }
        }
    }
}
