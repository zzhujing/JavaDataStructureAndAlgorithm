package com.concurrent.blockingqueue;

import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * {@link java.util.concurrent.DelayQueue}测试
 * 0. 元素必须
 * 1. 会按插入的时间排序{@link Delayed#compareTo(Object)}
 * 2. 使用迭代器会快速返回
 * 3. poll()的时候没有过期元素会直接返回null，take()会阻塞，peek()会直接返回未超时元素
 * 4. 不能插入null元素
 * 5.
 */
public class DelayQueueTest {


    @Test
    public void testAdd() throws InterruptedException {
        DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
        queue.add(DelayElement.of("Hello", 800));
        queue.add(DelayElement.of("World", 100));
        queue.add(DelayElement.of("Java", 500));
        long start = System.currentTimeMillis();
        assertThat(queue.take().getE(), equalTo("World"));
        System.out.println(System.currentTimeMillis() - start); //100
        assertThat(queue.take().getE(), equalTo("Java"));
        System.out.println(System.currentTimeMillis() - start); //500
        assertThat(queue.take().getE(), equalTo("Hello"));
        System.out.println(System.currentTimeMillis() - start); //800
    }

    @Test
    public void testReturnByIterator() {
        DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
        queue.add(DelayElement.of("Hello", 800));
        queue.add(DelayElement.of("World", 100));
        queue.add(DelayElement.of("Java", 500));
        Iterator<DelayElement<String>> it = queue.iterator();
        long start = System.currentTimeMillis();
        while (it.hasNext()) {
            it.next();
            assertThat((System.currentTimeMillis() - start) < 10, equalTo(true));
        }
    }

    @Test
    public void testPoll() throws InterruptedException {
        DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
        queue.add(DelayElement.of("Hello", 800));
        TimeUnit.MILLISECONDS.sleep(801);
        //poll()方法在没有过期元素的时候回直接返回null
        assertThat(queue.poll().getE(), equalTo("Hello"));
    }

    @Test
    public void testPeek() throws InterruptedException {
        DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
        queue.add(DelayElement.of("Hello", 800));
        assertThat(queue.peek().getE(), equalTo("Hello"));
    }

    @Test
    public void testRemove() throws InterruptedException {
        DelayQueue<DelayElement<String>> queue = new DelayQueue<>();
        queue.add(DelayElement.of("Hello", 800));
        TimeUnit.MILLISECONDS.sleep(801);
        assertThat(queue.remove().getE(), equalTo("Hello"));
    }

    static class DelayElement<E> implements Delayed {
        private final E e;
        private final long expireTime;

        static <E> DelayElement<E> of(E e, long delay) {
            return new DelayElement<>(e, delay);
        }

        DelayElement(E e, long delay) {
            this.e = e;
            this.expireTime = System.currentTimeMillis() + delay;
        }

        public E getE() {
            return e;
        }

        public long getExpireTime() {
            return expireTime;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expireTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            final DelayElement ele = (DelayElement) o;
            if (expireTime - ele.getExpireTime() > 0) {
                return 1;
            } else if (expireTime - ele.getExpireTime() < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
