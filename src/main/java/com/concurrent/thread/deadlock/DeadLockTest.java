package com.concurrent.thread.deadlock;

/**
 * 在两带锁的程序互相调用的情况下很容易死锁
 * 1. self.m1 获取self锁 去调用 other.o1获取到other锁
 * 2. 此时在o1还没有获取到锁的时候，另外一个线程调用other.o2获取到other锁并尝试调用self.m2 此时发现self锁被占用
 * 3. 此时两边都互相等待进入死锁
 * 如何诊断死锁呢？可以直接使用Java自带的jstack工具
 */
public class DeadLockTest {
    public static void main(String[] args) {
        OtherApplication other = new OtherApplication();
        SelfApplication self = new SelfApplication(other);
        other.setSelfApplication(self);

        new Thread(()->{
            while (true) {
                //去调用获取OtherApplication的锁
                self.m1();
            }
        }).start();

        new Thread(()->{
            while (true) {
                //OtherApplication来获取Self的锁
                other.o2();
            }
        }).start();
    }
}
