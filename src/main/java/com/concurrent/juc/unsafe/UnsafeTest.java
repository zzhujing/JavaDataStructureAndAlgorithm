package com.concurrent.juc.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试几种不同的同步方式的性能以及Unsafe的简单使用
 */
public class UnsafeTest {

    /**
     * Stupid
     * 9156989
     * Counter Increment spend Time :171ms
     * <p>
     * Synchronized
     * 100000000
     * Counter Increment spend Time :2522ms
     * <p>
     * Lock
     * 100000000
     * Counter Increment spend Time :3214ms
     * <p>
     * Atomic
     * 100000000
     * Counter Increment spend Time :2045ms
     * <p>
     * CustomUnsafe
     * 100000000
     * Counter Increment spend Time :15788ms
     */
    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {

        Counter counter = new CustomUnsafeCounter();
        final ExecutorService service = Executors.newFixedThreadPool(1000);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            service.submit(new CounterRunnable(counter, 100000));
        }
        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);
        long end = System.currentTimeMillis();
        System.out.println(counter.counter());
        System.out.println("Counter Increment spend Time :" + (end - start) + "ms");
    }

    interface Counter {
        void increment();

        long counter();
    }

    static class SyncCounter implements Counter {
        private long count = 0;

        @Override
        public synchronized void increment() {
            count++;
        }

        @Override
        public long counter() {
            return count;
        }
    }

    static class CustomUnsafeCounter implements Counter {
        private volatile long count = 0;
        private Unsafe unsafe;
        private long offset;

        CustomUnsafeCounter() throws NoSuchFieldException {
            unsafe = getUnsafe();
            offset = unsafe.objectFieldOffset(CustomUnsafeCounter.class.getDeclaredField("count"));
        }

        @Override
        public void increment() {
            long cur = count;
            while (!unsafe.compareAndSwapLong(this, offset, cur, cur + 1))
                cur = count;
        }

        @Override
        public long counter() {
            return count;
        }
    }

    static class AtomicCounter implements Counter {
        private volatile AtomicLong count = new AtomicLong();

        @Override
        public void increment() {
            count.incrementAndGet();
        }

        @Override
        public long counter() {
            return count.get();
        }
    }

    static class StupidCounter implements Counter {
        private long count = 0;

        @Override
        public void increment() {
            count++;
        }

        @Override
        public long counter() {
            return count;
        }
    }

    static class LockCounter implements Counter {
        private long count = 0;
        public final Lock lock = new ReentrantLock();

        @Override
        public void increment() {
            try {
                lock.lock();
                count++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public long counter() {
            return count;
        }
    }


    static class CounterRunnable implements Runnable {
        private final Counter counter;
        private final int num;

        CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                counter.increment();
            }
        }
    }


    /**
     * 使用反射获取Unsafe对象
     *
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return ((Unsafe) f.get(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
