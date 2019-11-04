package com.concurrent.design.threadlocal;

/**
 * @author : hujing
 * @date : 2019/11/4
 * 模拟执行任务
 */
public class ExecutionTask implements Runnable {

    private final QueryFromDbAction queryFromDbAction = new QueryFromDbAction();
    private final QueryFromHttpAction queryFromHttpAction = new QueryFromHttpAction();

    public ExecutionTask() {
    }

    @Override
    public void run() {
        //创建上下文对象
        final Context context = ActionContext.getInstance().get();
        //在一个任务中有多个业务需要用到的上下文对象可以使用ThreadLocal来在线程之间Share data
        queryFromDbAction.execute();
        queryFromHttpAction.execute();
        System.out.println(Thread.currentThread().getName() + " " + context.getCardId());
        System.out.println(Thread.currentThread().getName() + " " + context.getName());
    }
}
