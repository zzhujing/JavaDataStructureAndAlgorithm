package com.concurrent.design.suspension;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 保护性暂停模式，使用请求队列保存请求，为空就等待.
 */
public class SuspensionClient {
    public static void main(String[] args) throws InterruptedException {
        RequestQueue queue = new RequestQueue();
        ServerThread server = new ServerThread(queue);
        ClientThread client = new ClientThread(queue, "Guarded Suspension Pattern");
        server.start();
        client.start();
        Thread.sleep(10_000);
        server.close();
    }
}
