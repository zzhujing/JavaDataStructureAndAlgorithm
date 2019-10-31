package com.datastructure;

public class CircularQueueDemo {
    public static void main(String[] args) {
        CircularQueueDemo demo = new CircularQueueDemo(3);
        demo.enqueue(1);
        demo.enqueue(2);
        System.out.println("dequeue :" + demo.dequeue());
        demo.enqueue(3);
        System.out.println("dequeue :" + demo.dequeue());
        demo.enqueue(4);
        System.out.println("dequeue :" + demo.dequeue());
        demo.enqueue(5);
        demo.foreach();
    }

    private Object[] data;
    private int capacity;
    private int head;
    private int tail;

    public CircularQueueDemo(int capacity) {
        this.data = new Object[capacity];
        this.capacity = capacity;
        this.head = 0;
        this.tail = 0;
    }

    public void enqueue(Object object) {
        if ((tail + 1) % capacity == head) throw new IllegalArgumentException("queue full ");
        data[tail] = object;
        tail = (tail + 1) % capacity;
    }

    public Object dequeue() {
        if (tail == head) throw new IllegalArgumentException("queue empty");
        Object result = data[head];
        head = (head + 1) % capacity;
        return result;
    }

    public void foreach() {
        if (tail < head) {
            for (int i = head; i < capacity + tail; i++) {
                System.out.println(data[(i % capacity)]);
            }
        } else {
            for (int i = head; i < tail; i++) {
                System.out.println(data[i]);
            }
        }
    }
}
