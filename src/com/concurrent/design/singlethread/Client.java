package com.concurrent.design.singlethread;

/**
 * 测试单线程执行模式
 */
public class Client {
    public static void main(String[] args) {
        Gate gate = new Gate();
        new User("baobao","beijing",gate).start();
        new User("jj","jiangxi",gate).start();
        new User("hanglao","hangzhou",gate).start();
    }
}
