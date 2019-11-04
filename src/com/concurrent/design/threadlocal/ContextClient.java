package com.concurrent.design.threadlocal;

import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ContextClient {
    public static void main(String[] args) throws InterruptedException {
        IntStream.range(1,5)
                .forEach(i->new Thread(new ExecutionTask()).start());
    }
}
