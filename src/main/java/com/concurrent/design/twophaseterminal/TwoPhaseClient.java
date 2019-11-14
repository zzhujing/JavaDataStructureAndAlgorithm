package com.concurrent.design.twophaseterminal;

import java.io.IOException;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 使用一个简单的Socket交互来完成简单的两阶段模式(在线程结束的时候去释放一些资源.)
 */
public class TwoPhaseClient {
    public static void main(String[] args) throws InterruptedException, IOException {
/*        CounterIncrement ci = new CounterIncrement();
        ci.start();
        Thread.sleep(10_000);
        ci.close();*/
        TwoPhaseDemoServer server = new TwoPhaseDemoServer(11111);
        server.start();
        Thread.sleep(10_000);
        server.close();
    }
}
