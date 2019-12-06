package com.algorithm;


import java.util.LinkedList;

/**
 * 二叉树demo
 */
public class BinaryTreeDemo<E> {

    private final Node<E> root;

    public BinaryTreeDemo(Node<E> root) {
        this.root = root;
    }

    public static void main(String[] args) {
        Node<Integer> left4 = new Node<>(4123, null, null);
        Node<Integer> right3 = new Node<>(123, null, null);
        Node<Integer> left3 = new Node<>(24, null, null);
        Node<Integer> left2 = new Node<>(12, left4, null);
        Node<Integer> right2 = new Node<>(11, null, null);
        Node<Integer> left1 = new Node<>(2, left3, right3);
        Node<Integer> right1 = new Node<>(5, left2, right2);
        Node<Integer> root = new Node<>(3, left1, right1);
        BinaryTreeDemo<Integer> binaryTree = new BinaryTreeDemo<>(root);
        binaryTree.perOrder(root);
        System.out.println("-----");
        binaryTree.inOrder(root);
        System.out.println("------");
        binaryTree.postOrder(root);
        System.out.println("-----");
        binaryTree.levelForeach(root);
    }


    public <E> void perOrder(Node<E> node) {
        if (node == null) return;
        System.out.println(node.e);
        perOrder(node.left);
        perOrder(node.right);
    }

    public <E> void inOrder(Node<E> node) {
        if (node == null) return;
        inOrder(node.left);
        System.out.println(node.e);
        inOrder(node.right);
    }

    public <E> void postOrder(Node<E> node) {
        if (node == null) return;
        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.e);
    }

    //层序遍历->使用队列辅助
    public <E> void levelForeach(Node<E> node) {
        LinkedList<Node<E>> linkedList = new LinkedList<>();
        if (node != null) {
            linkedList.addLast(node);
        }
        while (!linkedList.isEmpty()) {
            final Node<E> e = linkedList.removeFirst();
            System.out.println(e.e);
            if (e.left != null) {
                linkedList.addLast(e.left);
            }
            if (e.right != null) {
                linkedList.addLast(e.right);
            }
        }
    }

    static class Node<E> {
        private E e;
        Node<E> left, right;

        public Node(E e, Node<E> left, Node<E> right) {
            this.e = e;
            this.left = left;
            this.right = right;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "e=" + e +
                    ", left=" + left +
                    ", right=" + right +
                    '}';
        }
    }
}
