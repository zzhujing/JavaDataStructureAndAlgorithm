package com.concurrent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author hujing
 * @date Create in 2020/9/7
 **/
public class LinkedListDemo {


    public static void main(String[] args) {
//        LinkedList result = LinkedList.of(1, 2, 3, 4, 5);
//        result.removeFirst();
//        result.removeFirst();
//        result.addLast(1);
//        result.addLast(2);
//        System.out.println(result);
//        result.reverse();
//        System.out.println(result);
//        result.getMidNode().stream().map(Node::getData).forEach(System.out::println);
//        LinkedList result = new LinkedList();
//        Node node1 = new Node(1);
//        node1.next = node1;
//        result.addLast(node1);
//        System.out.println(result.checkRing());

        LinkedList list = LinkedList.of(1, 2, 3, 2, 1);
        System.out.println(list);
//        list.removeNThNodeFromBottom(3);
//        list.removeNThNodeFromBottom(3);
//        list.removeNThNodeFromBottom(3);
//        list.removeNThNodeFromBottom(3);
//        list.removeNThNodeFromBottom(2);
//        list.removeNThNodeFromBottom(1);
//        System.out.println(list);
        System.out.println(list.checkPalindrome());
    }

    static class LinkedList {
        private Node head;
        private int size;

        public static LinkedList of(int... elements) {
            LinkedList result = new LinkedList();
            Arrays.stream(elements).forEach(result::addLast);
            return result;
        }


        //链表中环的检测
        public boolean checkRing() {
            if (isEmpty()) {
                return false;
            }

            Node slow = head;
            Node fast = head;

            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    return true;
                }
            }
            return false;
        }

        //两个有序的链表合并
        public LinkedList mergeLinkedList(LinkedList other) {
            if (other.isEmpty()) {
                return this;
            }
            if (isEmpty()) {
                return other;
            }

            //创建一个新的大链表
            LinkedList result = new LinkedList();

            Node left = head;
            Node right = other.head;
            while (left != null && right != null) {
                if (left.data <= right.data) {
                    result.addLast(left.data);
                    left = left.next;
                } else {
                    result.addLast(right.data);
                    right = right.next;
                }
            }

            if (left == null) {
                while (right != null) {
                    result.addLast(right.data);
                    right = right.next;
                }
            }

            while (left != null) {
                result.addLast(left.data);
                left = left.next;
            }
            return result;
        }


        //删除链表倒数第 n 个结点，用两距离为n的指针同时进行遍历，当遍历结束的时候后面的指针即为倒数第N个节点
        public void removeNThNodeFromBottom(int k) {
            if (k < 0 || k > size) {
                throw new IndexOutOfBoundsException(String.format("expect : #d , threshold : %d", k, size));
            }
            Node preFirst = null;
            Node first = head;
            Node second = head;
            for (int i = 0; i < k - 1; i++) {
                second = second.next;
            }

            while (second.next != null) {
                second = second.next;
                preFirst = first;
                first = first.next;
            }

            if (preFirst == null) {
                head = head.next;
            } else {
                //删除first
                preFirst.next = first.next;
            }
        }


        //检查是否是回文串
        public boolean checkPalindrome() {
            if (isEmpty()) {
                return false;
            }
            List<Node> midNode = getMidNode();
            assert midNode != null;
            if (midNode.size() == 1) {
                Node mid = midNode.get(0);
                Node right = LinkedList.reverse(mid.next);
                Node tmp = head;
                while (tmp.next != mid) {
                    if (tmp.data != right.data) {
                        return false;
                    }
                    tmp = tmp.next;
                    right = right.next;
                }
            } else {
                Node mid = midNode.get(1);
                Node right = LinkedList.reverse(mid.next);
                Node tmp = head;
                while (tmp.next != midNode.get(0)) {
                    if (tmp.data != right.data) {
                        return false;
                    }
                    tmp = tmp.next;
                    right = right.next;
                }
            }
            return true;
        }


        //获取中间节点，快慢指针，节点数是奇数 -> slow 节点数是偶数 -> slow and slow+1
        private List<Node> getMidNode() {
            if (head == null) return null;
            Node slow = head;
            Node fast = head;
            while (fast.next != null && fast.next.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            //奇数
            if (fast.next == null) {
                return Collections.singletonList(slow);
            }
            return Arrays.asList(slow, slow.next);
        }

        //链表反转
        private void reverse() {
            if (isEmpty()) return;
            Node pre = null;
            Node cur = head;
            Node next;
            while (cur != null) {
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            head = pre;
        }

        private static Node reverse(Node source) {
            if (source == null || source.next == null) {
                return source;
            }
            Node pre = null;
            Node cur = source;
            Node next;
            while (cur != null) {
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }


        private void addLast(int data) {
            Node newNode = new Node(data);
            if (isEmpty()) {
                head = newNode;
            } else {
                Node cur = head;
                while (cur.next != null) {
                    cur = cur.next;
                }
                cur.next = newNode;
            }
            size++;
        }

        private void addLast(Node newNode) {
            if (isEmpty()) {
                head = newNode;
            } else {
                Node cur = head;
                while (cur.next != null) {
                    cur = cur.next;
                }
                cur.next = newNode;
            }
            size++;
        }


        private int removeFirst() {
            if (head == null) {
                return -1;
            }
            Node tmp = head;
            head = tmp.next;
            size--;
            return tmp.data;
        }

        private void removeData(int data) {
            if (isEmpty()) {
                return;
            }
            Node pre = null;
            Node cur = head;
            while (cur != null) {
                if (cur.data == data) {
                    if (pre == null) {
                        head = null;
                    } else {
                        pre.next = cur.next;
                        cur = null;
                    }
                    return;
                }
                pre = cur;
                cur = cur.next;
            }
            size--;
        }

        public Node get(int data) {
            Node cur = head;
            while (cur != null) {
                if (cur.data == data) {
                    return cur;
                }
                cur = cur.next;
            }
            return null;
        }

        @Override
        public String toString() {
            Node cur = head;
            StringBuilder builder = new StringBuilder("[");
            while (cur != null) {
                builder.append(cur.data).append(",");
                cur = cur.next;
            }
            if (!isEmpty()) {
                builder.setCharAt(builder.length() - 1, ' ');
            }
            builder.append("]");
            return builder.toString();
        }


        public boolean isEmpty() {
            return head == null;
        }
    }

    static class Node {
        private int data;
        private Node next;

        public Node(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }
    }
}
