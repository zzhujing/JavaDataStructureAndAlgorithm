package com.datastructure;

public class QueueArrayDemo {
    private Object[] data;
    private int capacity;
    private int head = 0;
    private int tail = 0;

    public QueueArrayDemo(int capacity) {
        this.data = new Object[capacity];
        this.capacity = capacity;
    }

    public Object dequeue() {
        if (head == tail) {
            throw new IllegalArgumentException("队列空");
        }
        return data[head++];
    }

    public boolean enqueue(Object source) {
        if (tail == capacity) {
            if (head == 0) {
                //队列满了
                return false;
            }
            //若队列tail移动到尾端且容量未满则进行数组搬运，后面通过循环队列改进
            for (int i = head; i < tail; i++) {
                data[i - head] = data[i];
            }
            tail -= head;
            head = 0;
        }
        data[tail++] = source;
        return true;
    }

    public void foreach() {
        for (int i = head; i <tail ; i++) {
            System.out.println(data[i]);
        }
    }

    public static void main(String[] args) {
        QueueArrayDemo queueDemo = new QueueArrayDemo(3);
        System.out.println(queueDemo.enqueue(1));
        System.out.println(queueDemo.dequeue());
        System.out.println(queueDemo.enqueue(2));
        System.out.println(queueDemo.enqueue(2));
        System.out.println(queueDemo.dequeue());
        System.out.println(queueDemo.dequeue());
        System.out.println(queueDemo.enqueue(2));
        System.out.println(queueDemo.enqueue(2));
        queueDemo.foreach();
    }
}
