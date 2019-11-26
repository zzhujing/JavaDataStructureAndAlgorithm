package com.concurrent.blockingqueue;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * {@link SynchronousQueue} 没有内置的容量，在实现生产和消费者之间线程传递的时候是通过{@link SynchronousQueue#transferer}来直接实现数据的交换
 * 即那些获取但是不移除的方法都没用。
 */
public class SynchronousQueueTest {

    @Test(expected = IllegalStateException.class)
    public void testSynchronousQueue() {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        queue.add("Hello");
    }

    @Test
    public void testAddPassMethod() throws InterruptedException {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(queue::take, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(2);
        assertThat(queue.add("Hello"), equalTo(true));
        scheduledExecutorService.shutdown();
    }

    @Test
    public void testRemovePassMethod() throws InterruptedException {
        SynchronousQueue<String> queue = new SynchronousQueue<>();
        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> {
            try {
                queue.put("Hello");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(2);
        assertThat(queue.remove(), equalTo("Hello"));
        scheduledExecutorService.shutdown();
    }
}
