package com.datastructure;

import com.google.common.collect.Lists;

import java.util.*;

/**
 * 二叉查找树
 */
public class BinarySearchTreeDemo<E extends Comparable<E>> {

    private Node<E> root;

    public BinarySearchTreeDemo() {
    }

    public static void main(String[] args) {
        BinarySearchTreeDemo<Integer> binarySearchTree = new BinarySearchTreeDemo<>();
        binarySearchTree.addNode(3);
        binarySearchTree.addNode(12);
        binarySearchTree.addNode(32);
        binarySearchTree.addNode(423);
        binarySearchTree.addNode(54);
        binarySearchTree.addNode(2);
        binarySearchTree.addNode(32);
        binarySearchTree.addNode(54);
        System.out.println("before----------");
        binarySearchTree.levelOrder();
        System.out.println("delete");
        binarySearchTree.deleteNode(54);
        binarySearchTree.deleteNode(32);
        System.out.println("after --------");
        binarySearchTree.levelOrder();

    }

    //层序遍历
    public void levelOrder() {
        LinkedList<Node<E>> linkedList = new LinkedList<>();
        Node<E> tmp = root;
        if (tmp == null) return;
        linkedList.addLast(tmp);
        while (!linkedList.isEmpty()) {
            final Node<E> first = linkedList.removeFirst();
            System.out.println(first.e);
            if (first.left != null)
                linkedList.addLast(first.left);
            if (first.right != null)
                linkedList.addLast(first.right);

        }
    }

    /**
     * 添加节点，若a.compareTo(b) ，那么将继续在右子树中判断
     */
    public void addNode(E e) {
        if (e == null) throw new IllegalArgumentException("Element Not Be Null");
        Node<E> newNode = new Node<>(e, null, null);
        if (root == null) {
            root = newNode;
            return;
        }
        Node<E> tmp = root;
        while (true) {
            if (e.compareTo(tmp.e) >= 0)
                if (tmp.right != null)
                    tmp = tmp.right;
                else {
                    tmp.right = newNode;
                    return;
                }
            else {
                if (tmp.left != null)
                    tmp = tmp.left;
                else {
                    tmp.left = newNode;
                    return;
                }
            }
        }
    }

    /**
     * 查询所有值等于{e}的,并添加到集合中
     *
     * @param e
     */
    public LinkedList<Node<E>> findNode(E e) {
        LinkedList<Node<E>> result = Lists.newLinkedList();
        if (e == null) throw new IllegalArgumentException("Element Not Be Null");
        if (root == null) return null;
        Node<E> tmp = root;
        while (tmp != null) {
            if (e.compareTo(tmp.e) >= 0) {
                if (e.compareTo(tmp.e) == 0) {
                    result.push(tmp);
                }
                tmp = tmp.right;
            } else tmp = tmp.left;
        }
        return result;
    }

    /**
     * 删除树中所有的值等于e的节点，并返回
     *
     * @param e
     */
    public List<Node<E>> deleteNode(E e) {
        final LinkedList<Node<E>> deleteList = findNode(e);
        final ArrayList<Node<E>> result = new ArrayList<>();
        Collections.copy(deleteList, result);
        while (!deleteList.isEmpty())
            deleteOneNode(deleteList.pop());
        return result;
    }

    /**
     * 删除一个节点
     */
    private void deleteOneNode(Node<E> node) {
        //node为空。直接return
        if (node == null) return;
        if (node.left == null && node.right == null) {
            deleteWithNoChildNode(node);
        } else if (node.left != null && node.right != null) {
            deleteWithTwoChildNode(node);
        } else {
            deleteWithOneChildNode(node);
        }
    }

    /**
     * 删除含有left,right节点的节点
     */
    private void deleteWithTwoChildNode(Node<E> child) {
        if (child.left == null || child.right == null) throw new IllegalArgumentException("valid argument");
        if (root == null) return;
        Node<E> tmp = root;
        if (tmp == child) {
            //找到右子树的最小值并赋予tmp.left
            Node<E> rightMinNode = findRightMin(tmp);
            //删除该最小值节点
            deleteWithOneChildNode(rightMinNode);
            return;
        }
        while (tmp != null && (tmp.left != null || tmp.right != null)) {
            if (tmp.left == child) {
                //找到右子树的最小值并赋予tmp.left
                Node<E> rightMinNode = findRightMin(tmp.left);
                //删除该最小值节点
                deleteWithOneChildNode(rightMinNode);
                return;
            }
            if (tmp.right == child) {
                //找到右子树的最小值并赋予tmp.right
                Node<E> rightMinNode = findRightMin(tmp.right);
                //删除该最小值节点
                deleteWithOneChildNode(rightMinNode);
                return;
            }
            if (tmp.e.compareTo(child.e) > 0)
                tmp = tmp.left;
            else tmp = tmp.right;
        }
        System.out.println("没有这个节点！！");
    }

    //查找该节点的右子树最小节点
    private Node<E> findRightMin(Node<E> left) {
        Node<E> first = left.right;
        while (first.left != null) {
            first = first.left;
        }
        left.e = first.e;
        return first;
    }

    /**
     * 删除只有一个子节点的节点
     *
     * @param child 要删除的节点
     */
    private void deleteWithOneChildNode(Node<E> child) {
        if (root == null) return;
        Node<E> tmp = root;
        while (tmp != null && (tmp.left != null || tmp.right != null)) {
            if (tmp.left == child) {
                tmp.left = child.left == null ? child.right : child.left;
                return;
            }
            if (tmp.right == child) {
                tmp.right = child.left == null ? child.right : child.left;
                return;
            }
            if (tmp.e.compareTo(child.e) > 0)
                tmp = tmp.left;
            else tmp = tmp.right;
        }
        System.out.println("没有这个节点！！");
    }

    /**
     * 删除没有子节点的节点
     *
     * @param child 要删除的节点
     */
    private void deleteWithNoChildNode(Node<E> child) {
        if (root == null) return;
        if (root == child) {
            root = null;
            return;
        }
        Node<E> tmp = root;
        while (tmp != null && (tmp.left != null || tmp.right != null)) {
            if (tmp.left == child) {
                tmp.left = null;
                return;
            } else if (tmp.right == child) {
                tmp.right = null;
                return;
            }
            if (tmp.e.compareTo(child.e) > 0)
                tmp = tmp.left;
            else tmp = tmp.right;
        }
        System.out.println("没有这个节点！！");
    }

    /**
     * 查询一个节点的父节点
     */
    private Node<E> findParentNode(Node<E> child) {
        if (root == null) return null;
        if (root == child) return null;
        Node<E> tmp = root;
        while (tmp != null && (tmp.left != null || tmp.right != null)) {
            if (tmp.left == child || tmp.right == child) return tmp;
            if (tmp.e.compareTo(child.e) > 0)
                tmp = tmp.left;
            else tmp = tmp.right;
        }
        return null;
    }


    /**
     * 节点类
     *
     * @param <E>￿
     */
    static class Node<E extends Comparable<E>> {
        E e;
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
