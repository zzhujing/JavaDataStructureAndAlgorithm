package com.concurrent.blockingqueue;

import org.junit.Test;

import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 双向链表实现的BlockingQueue\
 * 相比{@link java.util.concurrent.LinkedBlockingQueue} 多了一些addFirst(),removeLast()等。。
 */
public class LinkedBlockingDequeTest {


    @Test
    public void testAdd() {
        LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>();
        deque.addFirst("Java");
        deque.addFirst("GoLang");
        assertThat(deque.removeLast(), equalTo("Java"));
        assertThat(deque.removeLast(), equalTo("GoLang"));
    }

    @Test
    public void testTakeFirst() throws InterruptedException {
        LinkedBlockingDeque<String> deque = new LinkedBlockingDeque<>();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> deque.addFirst("Hello"), 5, TimeUnit.SECONDS);
        executor.shutdown();
        assertThat(deque.takeFirst(), equalTo("Hello"));
    }
}
