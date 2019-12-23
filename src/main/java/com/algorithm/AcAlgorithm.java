package com.algorithm;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * AC自动机多模式匹配算法
 * 1. 是一种类似KMP的多模式字符串匹配算法
 * 2. 核心在于如何构建失败节点(类似KMP中的失效函数。)，KMP中是通过前面的前缀最长后缀已经算出，AC则是通过根节点的失败节点已经算出。
 * 那么当当前节点的子节点和当前节点的失败节点的字节点字符相同的时候，那么子节点的失败节点就是该失败节点的子节点，若不是则迭代直到失败节点为null
 */
public class AcAlgorithm {

    private AcNode root;

    public AcAlgorithm() {
        this.root = new AcNode();
    }

    public void buildTrieTree(List<String> listStr) {
        for (String s : listStr) {
            AcNode tmp = this.root;
            int length = 0;
            char[] chars = s.toCharArray();
            for (char c : chars) {
                int i = (int) (c - 'a');
                if (tmp.childArr[i] == null) {
                    AcNode newNode = new AcNode(c);
                    tmp.childArr[i] = newNode;
                }
                tmp = tmp.childArr[i];
                length++;
            }
            tmp.isEndingCharacter = true;
            tmp.length = length;
        }
    }

    /**
     * 构建整个AC自动机所有节点的失败节点
     */
    public void buildFailurePointer() {
        Queue<AcNode> queue = new LinkedList<>();
        this.root.fail = null; // 根节点的失败节点为null
        queue.add(root);
        //层序遍历，获取每个节点的失败节点。
        while (!queue.isEmpty()) {
            AcNode p = queue.remove();
            //遍历获取每个子节点的失败节点
            for (int i = 0; i < 26; i++) {
                AcNode pc = p.childArr[i];
                //为null继续拿下一个子字符元素
                if (pc == null) continue;
                //若为root，那么其子节点的失败节点就是root
                if (p == root) {
                    pc.fail = root;
                } else {
                    //不是root，则获取到其失败节点q
                    AcNode q = p.fail;
                    //当其不为null的时候，类似KMP迭代，比对其子节点的字符值是否在其失败节点的子节点中出现
                    while (q != null) {
                        AcNode qc = q.childArr[pc.val - 'a'];
                        //出现则其子节点的失败节点的就是其失败节点的对应字符值的子节点。
                        if (qc != null) {
                            pc.fail = qc;
                            break;
                        }
                        //失败节点的子节点数组中不存在该字符值的子节点，则拿失败节点的失败节点继续尝试直到其失败节点为null
                        q = q.fail;
                    }
                    if (q == null) pc.fail = root; //q为null的时候其fail失败节点就是root
                }
                //将子节点添加到队列中就行层次遍历。
                queue.add(pc);
            }
        }
    }

    /**
     * 从AC中匹配所有模式串，逐层向上匹配
     */
    public void match(char[] text) {
        int n = text.length;
        AcNode p = root;
        //遍历主串
        for (int i = 0; i < n; i++) {
            int idx = (int) (text[i] - 'a');
            //若子节点为null且不为root那么切换到其fail节点
            while (p.childArr[idx] == null && p != root) p = p.fail;
            //替换为子节点
            p = p.childArr[idx];
            //若没有匹配，那么从root开始
            if (p == null) p = root;
            AcNode tmp = p;
            while (tmp != root) {
                //当tmp为模式串结尾
                if (tmp.isEndingCharacter) {
                    int pos = i - tmp.length + 1;
                    System.out.println("匹配下标:" + pos + ", 长度 : " + tmp.length);
                }
                //获取树的上层最长前缀子串。
                tmp = tmp.fail;
            }
        }
    }

    public static void main(String[] args) {
        AcAlgorithm ac = new AcAlgorithm();
        List<String> asList = Lists.newArrayList("abcd", "bc", "bcd", "c");
        ac.buildTrieTree(asList);
        ac.buildFailurePointer();
        ac.match("abcd".toCharArray());
    }


    static class AcNode {
        private char val;
        private AcNode[] childArr;
        private int length = -1; //为模式串的长度
        private boolean isEndingCharacter = false; //是否是子叶节点
        private AcNode fail;

        public AcNode() {
            this.childArr = new AcNode[26];
        }

        public AcNode(char val) {
            this.val = val;
            this.childArr = new AcNode[26];
        }


        @Override
        public String toString() {
            return "AcNode{" +
                    "val=" + val +
                    ", childArr=" + Arrays.toString(childArr) +
                    ", length=" + length +
                    ", isEndingCharacter=" + isEndingCharacter +
                    ", fail=" + fail +
                    '}';
        }

        public char getVal() {
            return val;
        }

        public void setVal(char val) {
            this.val = val;
        }

        public AcNode[] getChildArr() {
            return childArr;
        }

        public void setChildArr(AcNode[] childArr) {
            this.childArr = childArr;
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public boolean isEndingCharacter() {
            return isEndingCharacter;
        }

        public void setEndingCharacter(boolean endingCharacter) {
            isEndingCharacter = endingCharacter;
        }

        public AcNode getFail() {
            return fail;
        }

        public void setFail(AcNode fail) {
            this.fail = fail;
        }
    }
}
