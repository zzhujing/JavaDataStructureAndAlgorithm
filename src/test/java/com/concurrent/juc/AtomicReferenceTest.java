package com.concurrent.juc;

import org.junit.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS中的 'ABA' 问题。
 */
public class AtomicReferenceTest {


    @Test
    public void testCreteAtomicReference() {
        AtomicReference<SimpleObject> atomicRef = new AtomicReference<>(new SimpleObject("hujing"));

        System.out.println(atomicRef.compareAndSet(atomicRef.get(), new SimpleObject("xcc")));
        SimpleObject badMan = new SimpleObject("bad man");
        System.out.println(atomicRef.compareAndSet(badMan, new SimpleObject("xcc")));
    }

    static class SimpleObject {

        private String name;

        public SimpleObject(String name) {
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
