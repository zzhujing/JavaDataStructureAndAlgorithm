package com.concurrent.blockingqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.fail;

/**
 * 基于数组的有界阻塞队列
 * 1. put(),take()方法分别会在队列满了和队列空时阻塞。
 * 2. 其中是通过ReentrantLock + Condition实现的
 */
public class ArrayBlockingQueueTest {

    private ArrayBlockingQueue<String> queue;

    @Before
    public void setUp() {
        queue = new ArrayBlockingQueue<>(2);
    }

    @After
    public void destroy() {
        queue = null;
    }

    @Test(expected = IllegalStateException.class)
    public void testArrayBlockingQueueAdd() {
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello6"), equalTo(false));
        fail("not be execute here now.");
    }

    @Test
    public void testOffer() {
        assertThat(queue.offer("Hello1"), equalTo(true));
        assertThat(queue.offer("Hello2"), equalTo(true));
        assertThat(queue.offer("Hello6"), equalTo(false));
    }

    @Test
    public void testPut() throws InterruptedException {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> queue.take(), 1, TimeUnit.SECONDS);
        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        executorService.shutdown();
    }

    @Test
    public void testTake() throws InterruptedException {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try {
                queue.put("Hello4");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, TimeUnit.SECONDS);
        queue.put("Hello1");
        queue.put("Hello2");
        assertThat(queue.take(), equalTo("Hello1"));
        assertThat(queue.take(), equalTo("Hello2"));
        assertThat(queue.take(), equalTo("Hello4"));
        System.out.println("aaa");
        executorService.shutdown();
    }


    @Test
    public void testPoll() throws InterruptedException {
        queue.put("Hello1");
        queue.put("Hello2");
        assertThat(queue.poll(), equalTo("Hello1"));
        assertThat(queue.poll(), equalTo("Hello2"));
        assertThat(queue.poll(), nullValue());
    }


    @Test
    public void testPeek() throws InterruptedException {
        queue.put("Hello1");
        queue.put("Hello2");
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
    }

    @Test
    public void testDrainTo() throws InterruptedException {
        queue.put("Hello1");
        queue.put("Hello2");
        List<String> tmp = new ArrayList<>();
        assertThat(queue.drainTo(tmp), equalTo(2));
        assertThat(tmp.size(), equalTo(2));
    }

    @Test(expected = NoSuchElementException.class)
    public void testElement() throws InterruptedException {
        queue.put("Hello1");
        queue.put("Hello2");
        assertThat(queue.remove(), equalTo("Hello1"));
        assertThat(queue.element(), equalTo("Hello1"));
        queue.clear();
        queue.element();
    }
}
