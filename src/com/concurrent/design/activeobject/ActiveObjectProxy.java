package com.concurrent.design.activeobject;

public class ActiveObjectProxy implements ActiveObject {

    private final SchedulerThread schedulerThread;
    private final Servant servant;

    public ActiveObjectProxy(SchedulerThread schedulerThread, Servant servant) {
        this.schedulerThread = schedulerThread;
        this.servant = servant;
    }

    @Override
    public Result makeString(int count, char filler) {
        FutureResult result = new FutureResult();
        //将方法Command添加到Command队列中,等待ActiveObject调用
        schedulerThread.invoke(new MakeStringRequest(servant, result, count, filler));
        return result;
    }

    @Override
    public void displayString(String text) {
        schedulerThread.invoke(new DisplayStringRequest(servant,text));
    }
}
