package com.concurrent.juc.concurrencycontainer;

public class PriorityLinkedList<E extends Comparable<E>> {
    private Node<E> first;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public static <E extends Comparable<E>> PriorityLinkedList<E> of(E... elements) {
        final PriorityLinkedList<E> result = new PriorityLinkedList<>();
        if (elements.length > 0) {
            for (E e : elements) {
                result.addSeq(e);
            }
        }
        return result;
    }

    public boolean contains(E e) {
        if (null != e) {
            PriorityLinkedList.Node<E> cur = first;
            while (cur != null) {
                if (cur.val == e) {
                    return true;
                }
                cur = cur.next;
            }
        }
        return false;
    }

    public PriorityLinkedList<E> addSeq(E e) {
        PriorityLinkedList.Node<E> newNode = new PriorityLinkedList.Node<>(e);
        Node<E> per = null;
        Node<E> cur = first;
        while (cur != null && e.compareTo(cur.val) > 0) {
            per = cur;
            cur = cur.next;
        }
        if (per == null) {
            first = newNode;
        } else {
            per.next = newNode;
        }
        newNode.next = cur;
        size++;
        return this;
    }


    public E removeFirst() {
        if (this.isEmpty()) throw new PriorityLinkedList.NoElementException("current linked list is empty");
        PriorityLinkedList.Node<E> tmp = first;
        first = tmp.next;
        size--;
        return tmp.val;
    }

    @Override
    public String toString() {
        if (null == first) return "[]";
        StringBuilder builder = new StringBuilder("[");
        PriorityLinkedList.Node<E> cur = first;
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
        private PriorityLinkedList.Node<E> next;

        public Node(E val) {
            this.val = val;
            next = null;
        }
    }

    public static void main(String[] args) {
        final PriorityLinkedList<String> list = PriorityLinkedList.of("Hello", "World", "Java", "GoLang", "Docker", "Spring");
        list.addSeq("Hj");
        System.out.println(list);
    }
}
