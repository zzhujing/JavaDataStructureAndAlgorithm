package com.concurrent.juc;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 使用Stamp和引用一起判断是否是这次的节点。
 */
public class AtomicStampReferenceTest {

    @Test
    public void testStampReference() throws InterruptedException {
        AtomicStampedReference<Simple> stampRef = new AtomicStampedReference<>(new Simple("hujing"), 0);
        final Simple xcc = new Simple("xcc");
        final Simple ref = stampRef.getReference();
        final int stamp = stampRef.getStamp();
        final Thread t1 = new Thread(() -> {
            /**
             * ABA
             */
            System.out.println(stampRef.compareAndSet(ref, xcc, stampRef.getStamp(), stampRef.getStamp() + 1)); //true
            System.out.println(stampRef.compareAndSet(xcc, ref, stampRef.getStamp(), stampRef.getStamp() + 1)); //true
        });
        t1.start();
        t1.join();
        System.out.println(stampRef.compareAndSet(ref, xcc, stamp, stamp + 1)); //false
    }

    @Test
    public void testOrdinalReference() throws InterruptedException {
        AtomicReference<Simple> atomicRef = new AtomicReference<>(new Simple("hujing"));
        final Simple xcc = new Simple("xcc");
        final Simple ref = atomicRef.get();
        final Thread t1 = new Thread(() -> {
            //此时经过多次改变，最后返回的引用不变,如果这里的Simple为栈或者队列，链表等数据结构则会有问题
            System.out.println(atomicRef.compareAndSet(ref, xcc)); //true
            System.out.println(atomicRef.compareAndSet(xcc, ref)); //true
        });
        t1.start();
        t1.join();
        System.out.println(atomicRef.compareAndSet(ref, xcc)); //true
    }


    static class Simple {
        private String name;

        public Simple(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
