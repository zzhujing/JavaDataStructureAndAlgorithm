package com.concurrent.design.activeobject;

/**
 * 真实Result
 */
public class RealResult implements Result {

    private final Object result;

    public RealResult(Object result) {
        this.result = result;
    }

    @Override
    public Object getResultValue() {
        return this.result;
    }
}
