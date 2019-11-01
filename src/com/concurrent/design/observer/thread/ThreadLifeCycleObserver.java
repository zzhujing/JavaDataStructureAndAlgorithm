package com.concurrent.design.observer.thread;

/**
 * @author : hujing
 * @date : 2019/11/1
 * 观察者
 */
public class ThreadLifeCycleObserver implements ThreadLifeCycleListener {

    @Override
    public void onEvent(SubjectRunnable.RunnableEvent event) {
        System.out.println(event.getExecuteThread() + " : " + event.getState() + " : " + event.getCause());
    }
}
