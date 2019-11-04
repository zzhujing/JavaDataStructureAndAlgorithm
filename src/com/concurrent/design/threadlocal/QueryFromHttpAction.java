package com.concurrent.design.threadlocal;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class QueryFromHttpAction {
    public void execute() {
        Context context = ActionContext.getInstance().get();
        String name = context.getName();
        String cardId = getCardId(name);
        context.setCardId(cardId);
    }

    public String getCardId(String name) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "123123" + Thread.currentThread().getId();
    }
}
