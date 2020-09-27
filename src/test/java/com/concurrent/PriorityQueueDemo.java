package com.concurrent;

import java.util.PriorityQueue;

/**
 * @author hujing
 * @date Create in 2020/9/24
 * java优先级队列Demo -> 小顶堆
 **/
public class PriorityQueueDemo {

    public static void main(String[] args) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(3);
        priorityQueue.add(2);
        priorityQueue.add(4);
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
        System.out.println(priorityQueue.poll());
    }

}
