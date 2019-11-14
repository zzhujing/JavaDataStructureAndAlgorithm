package com.concurrent.juc;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * AtomicInteger 之类的原子类型主要是通过{@link sun.misc.Unsafe#compareAndSwapInt(Object, long, int, int)}
 * 来进行CPU指令级别的比较和赋值
 */
public class AtomicIntegerTest {

    @Test
    public void testAtomic() {

        final AtomicInteger value = new AtomicInteger();

        IntStream.rangeClosed(1, 2)
                .forEach(i -> new Thread(() -> {
                    while (value.getAndIncrement() < 50) {
                        System.out.println(Thread.currentThread().getName() +"->"+value.get());
                    }
                }, String.valueOf(i)).start());
    }
}
