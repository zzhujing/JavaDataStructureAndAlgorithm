package com.concurrent.juc;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterTest {
    public static void main(String[] args) {

        AtomicReferenceFieldUpdater<Test, String> updater = AtomicReferenceFieldUpdater.newUpdater(Test.class, String.class, "i");

        final Test hello = new Test("Hello");

        System.out.println(updater.getAndSet(hello,"World"));

    }


    static class Test {
        protected volatile String i;

        public Test(String i) {
            this.i = i;
        }
    }
}
