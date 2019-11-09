package com.concurrent.design.work;

/**
 * Worker设计模式测试Demo
 */
public class WorkerPatternClient {
    public static void main(String[] args) {
        Channel channel = new Channel(10);
        channel.start();
        new TransportThread("hj",channel).start();
        new TransportThread("xcc",channel).start();
        new TransportThread("mom",channel).start();
    }
}
