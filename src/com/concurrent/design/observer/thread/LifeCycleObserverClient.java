package com.concurrent.design.observer.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public class LifeCycleObserverClient {
    public static void main(String[] args) {
        //监听类
        ThreadLifeCycleObserver listener = new ThreadLifeCycleObserver();

        //事件源
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<?>> futureList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            futureList.add(executorService.submit(new SubjectRunnableImpl(listener)));
        }
        boolean result = true;
        while (result) try {
            result = futureList.stream().map(Future::isDone).anyMatch(b -> !b);
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
