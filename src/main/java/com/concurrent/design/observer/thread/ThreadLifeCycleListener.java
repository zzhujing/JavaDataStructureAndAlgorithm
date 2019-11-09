package com.concurrent.design.observer.thread;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public interface ThreadLifeCycleListener {
    void onEvent(SubjectRunnable.RunnableEvent event);
}
