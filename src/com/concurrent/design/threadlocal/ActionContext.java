package com.concurrent.design.threadlocal;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 基于ThreadLocal的Action上下文对象
 */
public class ActionContext {
    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };
    private static ActionContext instance;

    //单例
    private ActionContext() {
    }



    public static ActionContext getInstance() {
        return ContextHolder.actionContext;
    }

    public Context get() {
        return threadLocal.get();
    }

    public void set(Context context){
        threadLocal.set(context);
    }

    //静态内部类
    private static class ContextHolder {
        private static ActionContext actionContext = new ActionContext();
    }

}
