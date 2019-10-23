package com.datastructure;

public class QueueLinkedListDemo {
    public static void main(String[] args) {
        QueueLinkedListDemo demo = new QueueLinkedListDemo();
        demo.enqueue(1);
        demo.enqueue(2);
        demo.enqueue(3);
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
        System.out.println(demo.dequeue());
        System.out.println("====");
        demo.foreach();
    }


    private Node head;
    private Node tail;

    public void enqueue(Object data) {
        Node node = new Node(data, null);
        if (tail == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    public Object dequeue() {
        if (head == null) throw new IllegalArgumentException("队列空的");
        Object result = head.data;
        head = head.next;
        return result;
    }

    public void foreach() {
        Node cur = head;
        while (cur != tail) {
            System.out.println(cur.data);
            cur = cur.next;
        }
        System.out.println(cur.data);
    }

    private static class Node {
        private Object data;
        private Node next;

        public Node(Object data, Node o) {
            this.data = data;
            this.next = o;
        }
    }
}
