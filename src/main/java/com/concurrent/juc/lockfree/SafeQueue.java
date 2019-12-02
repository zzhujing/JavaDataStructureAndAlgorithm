package com.concurrent.juc.lockfree;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * 使用CAS实现无锁线程安全队列
 */
public class SafeQueue<E> {

    private AtomicReference<Node<E>> head, tail;
    private AtomicInteger size;

    public SafeQueue() {
        Node<E> init = new Node<>(null);
        this.head = new AtomicReference<>(init);
        this.tail = new AtomicReference<>(init);
        size = new AtomicInteger(0);
    }

    public void addLast(E e) {
        Node<E> newNode = new Node<>(e);
        //这里其实做个两个操作，1.将tail->newNode 2.将赋值之前的tail引用副本返回
        Node<E> pre = tail.getAndSet(newNode);
        //把之前tail.next引用赋值给newNode完成addLast
        pre.next = newNode;
        //size++
        size.getAndIncrement();
    }

    public E removeFirst() {
        Node<E> headNode, valueNode;
        do {
            headNode = head.get();
            valueNode = headNode.next;
            //进行CAS判断是否是上次headNode
        } while (valueNode != null && !head.compareAndSet(headNode, valueNode));
        E e = (valueNode == null) ? null : valueNode.e;
        if (valueNode != null) {
            valueNode.e = null;
        }
        size.decrementAndGet();
        return e;
    }

    public static void main(String[] args) throws InterruptedException {
        SafeQueue<Long> queue = new SafeQueue<>();
        Random random = new Random(System.currentTimeMillis());
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ConcurrentHashMap<Long, Object> map = new ConcurrentHashMap<>();
        IntStream.range(0, 5).boxed().map(i -> (Runnable) () -> {
            for (int j = 0; j < 10; j++) {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                    queue.addLast(random.nextLong());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).forEach(executorService::submit);

        IntStream.range(0, 5).boxed().map(i -> (Runnable) () -> {
            try {
                int counter = 10;
                TimeUnit.MILLISECONDS.sleep(10);
                while (counter > 0) {
                    final Long remove = queue.removeFirst();
                    if (remove == null) continue;
                    map.put(remove, new Object());
                    counter--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).forEach(executorService::submit);
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
        System.out.println(map.size());
    }


    static class Node<E> {
        E e;
        volatile Node<E> next;

        public Node(E e) {
            this.e = e;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "e=" + e +
                    ", next=" + next +
                    '}';
        }
    }

}
