package com.concurrent;

/**
 * @author hujing
 * @date Create in 2020/9/9
 * 队列简单实现
 **/
public class QueueDemo {

    public static void main(String[] args) {
        //顺序队列
        ArrayQueue arrayQueue = new ArrayQueue(3);
        arrayQueue.enqueue(1);
        arrayQueue.enqueue(2);
        arrayQueue.enqueue(3);
        System.out.println(arrayQueue.dequeue());
        System.out.println(arrayQueue.dequeue());
        arrayQueue.enqueue(2);
        arrayQueue.enqueue(3);
//        arrayQueue.enqueue(3); IndexOutOfBounds

        //循环队列
        CycleQueue cycleQueue = new CycleQueue(3);
        cycleQueue.enqueue(1);
        System.out.println(cycleQueue.dequeue());
        cycleQueue.enqueue(2);
        System.out.println(cycleQueue.dequeue());
        cycleQueue.enqueue(2);
        System.out.println(cycleQueue.dequeue());
        cycleQueue.enqueue(3);
        System.out.println(cycleQueue.dequeue());
    }


    static class ArrayQueue {
        private final int[] data;
        private int capacity;
        private int head;
        private int tail;

        public ArrayQueue(int capacity) {
            this.capacity = capacity;
            this.data = new int[capacity];
            this.head = 0;
            this.tail = 0;
        }

        public int dequeue() {
            if (isEmpty()) {
                return -1;
            }
            return data[head++];
        }

        public void enqueue(int d) {
            if (tail == capacity) {
                if (head == 0) {
                    throw new IndexOutOfBoundsException();
                }
                //copy array
                for (int i = head; i < tail; i++) {
                    data[i - head] = data[i];
                }
                tail -= head;
                head = 0;
            }
            data[tail++] = d;
        }

        public boolean isEmpty() {
            return head == tail;
        }
    }

    static class CycleQueue {
        private final int[] data;
        private int head;
        private int tail;

        CycleQueue(int capacity) {
            this.data = new int[capacity];
        }

        public void enqueue(int d) {
            if (isFull()) {
                throw new IndexOutOfBoundsException();
            }
            data[tail] = d;
            tail = (tail + 1) % getCapacity();
        }

        public int dequeue() {
            if (isEmpty()) {
                return -1;
            }
            int result = data[head];
            head = (head + 1) % getCapacity();
            return result;
        }

        public boolean isEmpty() {
            return head == tail;
        }

        public boolean isFull() {
            return (tail + 1) % getCapacity() == head;
        }

        public int getCapacity() {
            return data.length;
        }
    }
}
