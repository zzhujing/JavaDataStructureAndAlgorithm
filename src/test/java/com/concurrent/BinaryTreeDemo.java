package com.concurrent;

import java.util.LinkedList;
import java.util.Stack;

/**
 * @author hujing
 * @date Create in 2020/9/16
 * 二叉树Demo
 **/
public class BinaryTreeDemo {

    public static void main(String[] args) {
        Node<Integer> root = buildTree();
//        preOrder(root);
//        preOrderWithoutRecursive(root);
//        inOrder(root);
//        inOrderWithoutRecursive(root);
//        postOrderWithoutRecursive(root); // 4 -> 5 -> 1 -> 9 -> 6 -> 3 -> 2
//        postOrder(root);
        levelTraversal(root);
    }


    /**
     * <pre>
     *        2
     *     /     \
     *    1       3
     *  /  \     /  \
     * 4    5   9    6
     * </pre>
     */
    public static Node<Integer> buildTree() {
        Node<Integer> root = new Node<>(2);
        root.left = new Node<>(1);
        root.right = new Node<>(3);
        root.left.left = new Node<>(4);
        root.left.right = new Node<>(5);
        root.right.right = new Node<>(6);
        root.right.left = new Node<>(9);
        return root;
    }

    /**
     * 前序(递归实现)
     * 2 -> 1 -> 4 -> 5 -> 3 -> 9 -> 6
     */
    public static void preOrder(Node<Integer> root) {
        if (root == null) {
            return;
        }
        System.out.println(root.data);
        preOrder(root.left);
        preOrder(root.right);
    }

    /**
     * 前序(非递归实现)
     */
    public static void preOrderWithoutRecursive(Node<Integer> root) {
        Stack<Node<Integer>> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<Integer> cur = stack.pop();
            System.out.println(cur.data);
            if (cur.right != null) {
                stack.push(cur.right);
            }
            if (cur.left != null) {
                stack.push(cur.left);
            }
        }
    }

    /**
     * 中序(递归)
     * 4 -> 1 -> 5 -> 2 -> 9 -> 3 -> 6
     */
    public static void inOrder(Node<Integer> root) {
        if (root == null) return;
        inOrder(root.left);
        System.out.println(root.data);
        inOrder(root.right);
    }

    /**
     * 中序(非递归)
     * 将左节点全部递归压栈直到null,出栈，打印，并将右节点压栈
     * 深度优先遍历
     */
    public static void inOrderWithoutRecursive(Node<Integer> root) {
        Stack<Node<Integer>> stack = new Stack<>();
        Node<Integer> cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                Node<Integer> pop = stack.pop();
                System.out.println(pop.data);
                cur = pop.right;
            }
        }
    }

    /**
     * 后序(递归)
     * 4 -> 5 -> 1 -> 9 -> 6 -> 3 -> 2
     */
    public static void postOrder(Node<Integer> root) {
        if (root == null) return;
        postOrder(root.left);
        postOrder(root.right);
        System.out.println(root.data);
    }

    /**
     * 后序(递归)
     * 左 -> 右 -> 根      => 问题转化成  根 -> 右 -> 左 然后逆序
     */
    public static void postOrderWithoutRecursive(Node<Integer> root) {
        Stack<Node<Integer>> stack = new Stack<>();
        Stack<Integer> storeStack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node<Integer> pop = stack.pop();
            storeStack.push(pop.data);
            if (pop.left != null) {
                stack.push(pop.left);
            }
            if (pop.right != null) {
                stack.push(pop.right);
            }
        }
        //print
        while (!storeStack.isEmpty()) {
            System.out.println(storeStack.pop());
        }
    }


    /**
     * 层序遍历
     * 广度优先遍历 ， 使用队列辅助
     */
    public static void levelTraversal(Node<Integer> root) {
        LinkedList<Node<Integer>> queue = new LinkedList<>();
        if (root == null) return;
        queue.addLast(root);
        while (!queue.isEmpty()) {
            Node<Integer> cur = queue.removeFirst();
            if (cur!=null) {
                System.out.println(cur.data);
                queue.addLast(cur.left);
                queue.addLast(cur.right);
            }
        }
    }


    static class Node<T> {
        private T data;
        private Node<T> left;
        private Node<T> right;

        public Node(T data) {
            this.data = data;
        }
    }
}
