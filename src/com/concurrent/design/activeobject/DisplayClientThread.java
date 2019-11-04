package com.concurrent.design.activeobject;

public class DisplayClientThread extends Thread {

    private final ActiveObject activeObject;

    public DisplayClientThread(ActiveObject activeObject, String name) {
        super(name);
        this.activeObject = activeObject;
    }

    @Override
    public void run() {
        while (true) {
            activeObject.displayString(Thread.currentThread().getName());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
