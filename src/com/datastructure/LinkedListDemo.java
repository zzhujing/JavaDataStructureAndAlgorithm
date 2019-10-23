package com.datastructure;


public class LinkedListDemo {
    public static void main(String[] args) {
        SingleLinkedList linkedList = new SingleLinkedList();
        linkedList.add("1");
        linkedList.add("2");
        linkedList.add("3");
        System.out.println(linkedList.deleteLastNonNode(0));
    }


    static class SingleLinkedList {
        private Node head;
        private Node last;
        private int size;

        public SingleLinkedList() {
        }

        public SingleLinkedList(Node head) {
            this.head = head;
        }

        //链表中环的检测,快慢指针追击相遇问题
        public boolean checkLinkedRing() {
            if (head == null || head.next == null || head.next.next == null) return false;
            Node slow = head;
            Node quick = head;
            while (quick.next != null && quick.next.next != null) {
                slow = slow.next;
                quick = quick.next.next;
                if (slow == quick) {
                    return true;
                }
            }
            return false;
        }


        // 两个有序的链表合并,非递归
        public Node merge(Node left, Node right) {
            if (left == null && right == null) return null;
            if (right == null) return left;
            if (left == null) return right;
            while (left != null && right != null) {
                if (left.data.compareTo(right.data) > 0) {
                    add(right.data);
                    right = right.next;
                } else if (left.data.compareTo(right.data) == 0) {
                    add(left.data);
                    add(right.data);
                    left = left.next;
                    right = right.next;
                } else {
                    add(left.data);
                    left = left.next;
                }
            }
            if (left != null) {
                add(left);
            } else if (right != null) {
                add(right);
            }
            return head;
        }

        //递归合并两个有序链表的值
        public Node mergeRecursive(Node left, Node right) {
            if (left == null && right == null) return head;
            if (left == null) {
                add(right);
                return head;
            }
            if (right == null) {
                add(left);
                return head;
            }
            if (left.data.compareTo(right.data) > 0) {
                add(right.data);
                return mergeRecursive(left, right.next);
            } else if (left.data.compareTo(right.data) == 0) {
                add(left.data);
                add(right.data);
                return mergeRecursive(left.next, right.next);
            } else {
                add(left.data);
                return mergeRecursive(left.next, right);
            }
        }

        //删除链表倒数第n个结点
        public Node deleteLastNonNode(int n) {
            if (head == null) throw new NullPointerException("node size cannot be zero");
            //1. 先逆序
            add("");
            Node flip = flip(head);
            //2. 遍历删除第n个节点
            if (n <= 0) throw new IllegalArgumentException("参数错误");
            Node pre = null;
            Node cur = flip;
            while (cur.next != null && n != 0) {
                pre = cur;
                cur = cur.next;
                n--;
            }
            if (cur.next == null && n != 0) throw new IllegalArgumentException("没有那么多元素");
            pre.next = pre.next.next;
            return flip.next;
        }


        //求链表的中间结点
        public Node getMidNode() {
            if (head == null) return null;
            if (head.next == null) return head;
            //快慢指针求中间节点
            Node slow = head;
            Node quick = head;
            while (quick.next != null && quick.next.next != null) {
                slow = slow.next;
                quick = quick.next.next;
            }
            if (quick.next == null) {
                //奇数
                return slow;
            } else {
                slow.data = slow.data + quick.data;
                return slow;
            }
        }

        public void add(Node node) {
            if (head == null) head = node;
            last.next = node;
            last = node;
        }

        public void add(String data) {
            Node newNode = new Node(data, null);
            if (head == null) {
                last = newNode;
                head = newNode;
            } else {
                //添加节点到末尾
                last.next = newNode;
                last = newNode;
            }
            size++;
        }


        public void addHead(String data) {
            Node newNode = new Node(data, null);
            Node tmp = head;
            head = newNode;
            newNode.next = tmp;
            size++;
        }

        public void foreach() {
            if (head == null) return;
            Node cur = head;
            while (cur.next != null) {
                System.out.print(cur.data + " ,");
                cur = cur.next;
            }
            System.out.println(cur.data);
        }

        public boolean checkBackToText() {
            //1. 快慢指针获取中节点
            if (head == null) throw new IllegalArgumentException("非法参数异常");
            if (head.next == null) return true;
            Node slow = head;
            Node quick = head;
            while (quick.next != null && quick.next.next != null) {
                slow = slow.next;
                quick = quick.next.next;
            }
            Node left = head;

            //翻转后半节点部分，这里不需要区分奇数偶数，因为奇数的时候left和right中间多一个节点不需要判断所以翻转slow.next即可，偶数的时候直接翻转slow.next即可
            Node right = flip(slow.next);
            //2. 翻转后半部分节点
            //3. 遍历比较，这里left和right节点肯定是一样多的。所以直接用right来判断
            while (right != null) {
                if (left.data.equals(right.data)) {
                    left = left.next;
                    right = right.next;
                } else {
                    return false;
                }
            }
            return true;
        }

        /**
         * 单链表反转 -> 逐个遍历，并将其next指针指向前一个节点
         *
         * @param source 开始节点
         * @return 翻转后的node节点
         */
        public Node flip(Node source) {
            if (source == null) throw new IllegalArgumentException("error argument");
            if (source.next == null) return source;
            Node cur = source;
            Node pre = null;
            Node next = null;
            while (cur != null) {
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }


        private static class Node {
            private String data;
            private Node next;

            public Node(String data, Node next) {
                this.data = data;
                this.next = next;
            }

            @Override
            public String toString() {
                return "Node{" +
                        "data='" + data + '\'' +
                        ", next=" + next +
                        '}';
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public Node getNext() {
                return next;
            }

            public void setNext(Node next) {
                this.next = next;
            }
        }
    }
}
