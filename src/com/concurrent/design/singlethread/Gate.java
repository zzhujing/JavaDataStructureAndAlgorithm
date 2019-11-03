package com.concurrent.design.singlethread;


/**
 * 单线程模式保证线程安全
 * <p>
 * Gate 表示公共资源
 */
public class Gate {
    private String name;
    private String address;
    private int count;

    /**
     * 竞争资源
     */
    public synchronized void pass(String name, String address) {
        this.name = name;
        this.address = address;
        this.count++;
        verify();
    }

    private void verify() {
        if (name.charAt(0) != address.charAt(0)) {
            System.out.println("error -> " + toString());
        }
    }

    public String toString() {
        return "NO:" + count + ", name :" + name + ", address :" + address;
    }
}