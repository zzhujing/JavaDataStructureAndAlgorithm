package com.concurrent.design.activeobject;

public class ActiveObjectFactory {


    private ActiveObjectFactory() {
    }

    public static ActiveObject createActiveObject() {
        Servant servant = new Servant();
        ActivationQueue queue = new ActivationQueue();
        SchedulerThread schedulerThread = new SchedulerThread(queue);
        schedulerThread.start();
        return new ActiveObjectProxy(schedulerThread, servant);
    }
}
