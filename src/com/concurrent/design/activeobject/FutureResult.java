package com.concurrent.design.activeobject;


/**
 * FutureResult -> 通过setReResult,设置RealResult进来
 */
public class FutureResult implements Result {

    private Result result;
    private volatile boolean ready = false;

    public synchronized void setResult(Result result) {
        this.result = result;
        this.ready = true;
        this.notifyAll();
    }

    @Override
    public synchronized Object getResultValue() {
        while (!ready) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return this.result.getResultValue();
    }
}
