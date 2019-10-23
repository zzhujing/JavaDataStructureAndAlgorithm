package com.datastructure;

public class QueueDemo {
    private Object[] data;
    private int capacity;
    private int head = 0;
    private int tail = 0;

    public QueueDemo(int capacity) {
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
        QueueDemo queueDemo = new QueueDemo(3);
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
