package com.concurrent.design.observer.thread;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public class SubjectRunnableImpl extends SubjectRunnable {

    public SubjectRunnableImpl(ThreadLifeCycleListener listener) {
        super(listener);
    }

    @Override
    public void run() {
        try {
            notifyChange(new RunnableEvent(RunnableState.RUNNING, Thread.currentThread(), null));
            Thread.sleep(1000);
            int i = 1 / 0;
            notifyChange(new RunnableEvent(RunnableState.DONE, Thread.currentThread(), null));
        } catch (Throwable e) {
            notifyChange(new RunnableEvent(RunnableState.ERROR, Thread.currentThread(), e.getCause()));
        }
    }
}
