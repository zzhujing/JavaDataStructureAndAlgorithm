package com.datastructure;


import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author hujing
 * @date Create in 2020/9/18
 * 二叉查找树(支持重复值)
 * - 重复值则继续加入到右子树
 * - 查询，遍历查询所有满足的值(相同继续cur = cur.right)
 * - 删除,查询具体出现的次数，然后逐次删除
 **/
public class BinarySearchTree {

    private TreeNode root;

    public BinarySearchTree(int val) {
        this.root = new TreeNode(val);
    }

    public static void main(String[] args) {
        BinarySearchTree binarySearchTree = new BinarySearchTree(5);
        binarySearchTree.add(2);
        binarySearchTree.add(3);
        binarySearchTree.add(7);
        binarySearchTree.add(9);
        binarySearchTree.add(6);
        binarySearchTree.add(8);
        binarySearchTree.add(11);
        binarySearchTree.add(8);
        binarySearchTree.traversal();
        binarySearchTree.remove(3);
        binarySearchTree.remove(34);
        binarySearchTree.removeAllNode(8);
        binarySearchTree.traversal();
    }

    /**
     * 添加 , 若数据重复则当成大于的数
     *
     * @param val data value
     */
    public void add(int val) {
        TreeNode newNode = new TreeNode(val);
        if (root == null) { //空树
            root = newNode;
            return;
        }
        TreeNode cur = root;
        for (; ; ) { //循环遍历
            if (cur.val > val) { //小于当前节点值则走左子树
                if (cur.left == null) { //找到添加位置
                    cur.left = newNode;
                    return;
                }
                cur = cur.left;
            } else {
                //相同重复值继续在右子树中进行添加
                if (cur.right == null) {
                    cur.right = newNode;
                    return;
                }
                cur = cur.right;
            }
        }
    }

    /**
     * 查询
     *
     * @param val TreeNode#val
     */
    public TreeNode find(int val) {
        if (root == null) return null;
        TreeNode cur = root;
        while (cur != null) {
            if (cur.val > val) {
                cur = cur.left;
            } else if (cur.val < val) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    /**
     * 支持查询重复值
     *
     * @param val TreeNode#val
     */
    public List<TreeNode> findAllNode(int val) {
        if (root == null) return Collections.emptyList();
        List<TreeNode> result = Lists.newArrayList();
        TreeNode cur = root;
        while (cur != null) {
            if (cur.val > val) {
                cur = cur.left;
            } else {
                if (cur.val == val) {
                    result.add(cur);
                }
                cur = cur.right;
            }
        }
        return result;
    }

    /**
     * 移除所有val对应的节点
     *
     * @param val
     */
    public void removeAllNode(int val) {
        List<TreeNode> allNode = findAllNode(val);
        if (allNode.size() == 0) return;
        IntStream.rangeClosed(1, allNode.size()).forEach(i -> remove(val));
    }

    /**
     * 删除节点
     * 找到要删除的节点 p , (记录上级节点 pp)
     * 若其有两个子节点,则获取到右子树的最小节点minP 和记录其上级(minPP) (递归获取右子树的左节点),交换minP和p的值。pp = minPP p = minP (重用删除一个节点或者无子节点的逻辑)
     * 若有一个子节点，则直接让该 pp指向p的指向子节点即可
     * 若没有子节点，则直接让pp指向null即可
     *
     * @param val 节点val
     */
    public void remove(int val) {
        if (root == null) return;
        //find val
        TreeNode p = root;
        TreeNode pp = null;
        while (p != null) {
            if (val > p.val) {
                pp = p;
                p = p.right;
            } else if (val < p.val) {
                pp = p;
                p = p.left;
            } else {
                break;
            }
        }
        if (p == null) return;

        //删除两个节点
        if (p.left != null && p.right != null) {
            //获取右子树中的最小节点
            TreeNode minP = p.right;
            TreeNode minPP = p;
            while (minP.left != null) {
                minP = minP.left;
                minPP = minP;
            }
            p.val = minP.val;
            //这一步的结果是，交换了需要删除节点和右子树最小节点之后只需要删除最小节点即可
            //于是乎将 minPP minP -> pp p执行删除只有一个子节点或者没有子节点的逻辑
            pp = minPP;
            p = minP;
        }

        TreeNode tmp;
        if (p.left != null && p.right == null) {
            tmp = p.left;
        } else if (p.left == null && p.right != null) {
            tmp = p.right;
        } else {
            tmp = null;
        }

        //将pp指向tmp
        if (pp == null) {
            root = p;
        } else if (pp.left == p) {
            pp.left = tmp;
        } else {
            pp.right = tmp;
        }
    }


    static class TreeNode {

        private int val;
        private TreeNode left;
        private TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }


    public void traversal() {
        LinkedList<TreeNode> queue = new LinkedList<>();
        queue.addLast(this.root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.removeFirst();
            System.out.println(cur.val);
            if (cur.left != null) {
                queue.addLast(cur.left);
            }
            if (cur.right != null) {
                queue.addLast(cur.right);
            }
        }
    }
}
