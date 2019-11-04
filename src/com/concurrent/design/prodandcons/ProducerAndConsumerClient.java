package com.concurrent.design.prodandcons;

/**
 * @author : hujing
 * @date : 2019/11/4
 */
public class ProducerAndConsumerClient {

    public static void main(String[] args) {

        //创建消息队列
        MessageQueue queue = new MessageQueue();

        //启动多个生产者
        new ProducerThread(queue,1).start();
        new ProducerThread(queue,2).start();
        new ProducerThread(queue,3).start();

        //启动消费者
        new ConsumerThread(queue,1).start();
    }
}
