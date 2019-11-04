package com.concurrent.design.activeobject;


/**
 * 接受异步消息的主动方法
 */
public interface ActiveObject {

    Result makeString(int count, char filler);

    void displayString(String text);

}
