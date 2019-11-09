package com.concurrent.design.observer.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : hujing
 * @date : 2019/11/1
 * 观察者模式中的事件源
 */
public class Subject {

    //要通知的观察者对象列表
    private final List<Observer> observerList;

    //改变的属性
    private int state;

    public Subject() {
        observerList = new ArrayList<>();
    }

    public int getState() {
        return state;
    }

    public void attach(Observer observer) {
        Objects.requireNonNull(observer);
        observerList.add(observer);
    }

    public void setState(int state) {
        //如果状态没变化那么不需要通知
        if (this.state == state) return;
        this.state = state;
        notifyAllObserver();
    }

    private void notifyAllObserver() {
        observerList.forEach(Observer::update);
    }
}
