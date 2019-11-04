package com.concurrent.design.activeobject;

public class MakeClientThread extends Thread {

    private final ActiveObject activeObject;
    private final char filler;

    public MakeClientThread(ActiveObject activeObject, char filler) {
        this.activeObject = activeObject;
        this.filler = filler;
    }

    @Override
    public void run() {
        while (true) {
            Result result = activeObject.makeString(5, filler);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " -> " +result.getResultValue());
        }
    }
}
