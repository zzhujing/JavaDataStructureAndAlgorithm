package com.concurrent.juc.lockfree;


/**
 * 自定义线程不安全队列
 *
 * @param <E>
 */
public class UnsafeQueue<E> {

    public static void main(String[] args) {
        UnsafeQueue<String> queue = new UnsafeQueue<>();
        queue.addLast("Java");
        queue.addLast("GoLang");
        queue.addLast("Python");
        System.out.println(queue.peekFirst());
        System.out.println(queue.peekLast());
    }

    private Node<E> head, tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public E peekFirst() {
        return (head == null) ? null : head.e;
    }

    public E peekLast() {
        return (tail == null) ? null : tail.e;
    }

    public void addLast(E e) {
        if (e == null) throw new NullPointerException();
        Node<E> newNode = new Node<>(e, null);
        Node<E> tmp = tail;
        tail = newNode;
        if (size == 0) {
            head = newNode;
        } else {
//            tail.next = newNode;
//            tail = newNode;
            tmp.next = newNode;
        }
        size++;
    }

    public E removeFirst() {
        Node<E> tmp = head;
        if (tmp == null) return null;
        E remove = tmp.e;
        head = head.next;
        if (--size == 0)
            tail = null;
        return remove;
    }

    static class Node<E> {
        private E e;
        private Node<E> next;

        public Node(E e, Node<E> next) {
            this.e = e;
            this.next = next;
        }
    }
}
