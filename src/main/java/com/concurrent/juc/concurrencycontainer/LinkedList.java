package com.concurrent.juc.concurrencycontainer;

/**
 * 自定义LinkedList
 */
public class LinkedList<E> {


    private Node<E> first;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static <E> LinkedList<E> of(E... elements) {
        final LinkedList<E> result = new LinkedList<>();
        if (elements.length > 0) {
            for (E e : elements) {
                result.addFirst(e);
            }
        }
        return result;
    }

    public boolean contains(E e) {
        if (null != e) {
            Node<E> cur = first;
            while (cur != null) {
                if (cur.val == e) {
                    return true;
                }
                cur = cur.next;
            }
        }
        return false;
    }

    public LinkedList<E> addFirst(E e) {
        Node<E> newNode = new Node<>(e);
        Node<E> tmp = first;
        first = newNode;
        newNode.next = tmp;
        size++;
        return this;
    }

    public E removeFirst() {
        if (this.isEmpty()) throw new NoElementException("current linked list is empty");
        Node<E> tmp = first;
        first = tmp.next;
        size--;
        return tmp.val;
    }

    @Override
    public String toString() {
        if (null == first) return "[]";
        StringBuilder builder = new StringBuilder("[");
        Node<E> cur = first;
        while (cur != null) {
            builder.append(cur.val).append(",");
            cur = cur.next;
        }
        builder.replace(builder.length() - 1, builder.length(), "]");
        return builder.toString();
    }

    static class NoElementException extends RuntimeException {
        public NoElementException(String message) {
            super(message);
        }
    }


    static class Node<E> {
        private E val;
        private Node<E> next;

        public Node(E val) {
            this.val = val;
            next = null;
        }
    }

    public static void main(String[] args) {
        LinkedList<String> list = LinkedList.of("Hello", "World", "Java", "GoLang", "Python");
        Node<String> tmp = list.first;
        tmp = null;
        System.out.println(tmp);
        System.out.println(list.first);
    }
}
