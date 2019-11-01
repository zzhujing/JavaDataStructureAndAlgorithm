package com.concurrent.design.observer.basic;

/**
 * @author : hujing
 * @date : 2019/11/1
 */
public abstract class Observer {

    //事件源对象
    protected Subject subject;

    public Observer(Subject subject) {
        this.subject = subject;
        this.subject.attach(this);
    }

    protected abstract void update();
}
