package com.concurrent.design.activeobject;


/**
 * Active Object 测试类
 */
public class ActiveObjectClient {
    public static void main(String[] args) {
        ActiveObject activeObject = ActiveObjectFactory.createActiveObject();
        new MakeClientThread(activeObject,'*').start();
        new MakeClientThread(activeObject,'*').start();
        new MakeClientThread(activeObject,'*').start();
        new DisplayClientThread(activeObject,"*****").start();
    }
}
