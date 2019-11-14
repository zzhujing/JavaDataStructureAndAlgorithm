package com.concurrent.juc;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * AtomicXXXFieldUpdater其实就是代理一个类中的某个属性做原子操作
 * <p>
 * 注意点：
 * 1. 属性必须为volatile修饰
 * 2. 属性不能为static
 * 3. 引用类型需要使用{@link java.util.concurrent.atomic.AtomicReferenceFieldUpdater}
 * 4. 字段名不能valid
 *
 * 为什么需要使用xxFieldUpdater去代替AtomicXxx
 *
 * 1. 需要给其他类的属性拥有原子性
 * 2. 使用AtomicXxx需要创建多次，更加耗费内存
 * 3. 不想使用锁（显示lock，synchronized）
 */
public class AtomicXXFieldUpdaterTest {

    private volatile Integer i;
    private final AtomicIntegerFieldUpdater<AtomicXXFieldUpdaterTest> updater = AtomicIntegerFieldUpdater.newUpdater(AtomicXXFieldUpdaterTest.class, "i");

    @Test
    public void testFieldUpdater() {

        AtomicXXFieldUpdaterTest test = new AtomicXXFieldUpdaterTest();

        for (int j = 0; j < 2; j++) {
            new Thread(() -> {
                final int MAX = 20;
                while (updater.getAndIncrement(test) < MAX) {
                    System.out.println(Thread.currentThread().getName() + "->" + updater.get(test));
                }
            }).start();
        }
    }
}
