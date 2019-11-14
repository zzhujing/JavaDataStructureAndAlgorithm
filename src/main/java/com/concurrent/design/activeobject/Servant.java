package com.concurrent.design.activeobject;


/**
 * 具体方法执行者
 */
public class Servant implements ActiveObject {
    @Override
    public Result makeString(int count, char filler) {
        char[] buf = new char[count];
        for (int i = 0; i < count; i++) {
            buf[i] = filler;
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new RealResult(new String(buf));
    }


    @Override
    public void displayString(String text) {
        System.out.println(text);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
