package com.concurrent.design.observer.thread;

/**
 * @author : hujing
 * @date : 2019/11/1
 * 线程生命周期事件源（被观察者）
 */
public abstract class SubjectRunnable implements Runnable {

    //生命周期监听,一般为一个List
    protected ThreadLifeCycleListener listener;

    public SubjectRunnable(ThreadLifeCycleListener listener) {
        this.listener = listener;
    }
    /**
     * 发生状态改变后主动调用的通知方法
     *
     * @param event 事件源事件(信息携带)
     */
    public void notifyChange(RunnableEvent event) {
        listener.onEvent(event);
    }

    /**
     * 状态枚举
     */
    public enum RunnableState {
        RUNNING, DONE, ERROR;
    }

    public static class RunnableEvent {
        private final RunnableState state;
        private final Thread executeThread;
        private final Throwable cause;

        protected RunnableEvent(RunnableState state, Thread executeThread, Throwable cause) {
            this.state = state;
            this.executeThread = executeThread;
            this.cause = cause;
        }

        public RunnableState getState() {
            return state;
        }

        public Thread getExecuteThread() {
            return executeThread;
        }

        public Throwable getCause() {
            return cause;
        }
    }
}
