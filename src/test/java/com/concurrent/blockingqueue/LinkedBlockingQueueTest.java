package com.concurrent.blockingqueue;


import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 基于链表的可选边界的阻塞队列
 */
public class LinkedBlockingQueueTest {

    @Test
    public void testAdd() throws InterruptedException {
        List<String> source = Lists.newArrayList("Hello", "World", "Java");
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(source);
        System.out.println(queue.take());
        System.out.println(queue.take());
        System.out.println(queue.take());
    }
}
