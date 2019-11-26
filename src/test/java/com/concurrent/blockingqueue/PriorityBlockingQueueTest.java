package com.concurrent.blockingqueue;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 优先阻塞队列
 * 1. 在ArrayBlockingQueue的基础上加了排序
 */
public class PriorityBlockingQueueTest {
    @Test
    public void testAddMoreThenInitCapacity() {
        PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(2);
        queue.offer("Hello");
        queue.offer("World");
        queue.offer("Java");
        assertThat(queue.size(), equalTo(3));
    }

    @Test(expected = ClassCastException.class)
    public void testElementNoComparable() {
        PriorityBlockingQueue<NoComparableObj> queue = new PriorityBlockingQueue<>(2);
        queue.add(new NoComparableObj());
    }

    @Test
    public void testTakeMethod() throws InterruptedException {
        PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(2);
        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> queue.offer("HM"), 2, TimeUnit.SECONDS);
        queue.offer("Hello");
        queue.offer("World");
        assertThat(queue.take(), equalTo("Hello"));
        assertThat(queue.take(), equalTo("World"));
        assertThat(queue.take(), equalTo("HM"));
        scheduledExecutorService.shutdown();
    }


    static class NoComparableObj {

    }
}
