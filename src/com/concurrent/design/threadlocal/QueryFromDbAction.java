package com.concurrent.design.threadlocal;

import javax.swing.*;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class QueryFromDbAction {

    public void execute() {
        String name = "hj" + Thread.currentThread().getName();
        ActionContext.getInstance().get().setName(name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
