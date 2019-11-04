package com.concurrent.design.threadlocal;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class Context {


    private String cardId;
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }
}
