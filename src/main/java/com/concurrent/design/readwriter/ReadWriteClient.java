package com.concurrent.design.readwriter;

public class ReadWriteClient {
    public static void main(String[] args) {
        ShareData data = new ShareData(10);
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new ReadWorker(data).start();
        new WriteWorker(data,"ANCDEFGHIJKOMNOPQRSTUVWSYZ").start();
        new WriteWorker(data,"ancdefghijkomnopqrstuvwsyz").start();
    }
}
