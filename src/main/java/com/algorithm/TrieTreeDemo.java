package com.algorithm;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * 字典树demo,像我们常见的搜索引擎里面的字符串输入匹配就是用这个的优化版来进行完成的。
 * 他相比于散列表，红黑树更加擅长于查找固定前缀的所有子串。但是会有大量的内存消耗。
 */
public class TrieTreeDemo {

    private TrieTree root;

    public TrieTreeDemo() {
        this.root = new TrieTree(26, true);
    }


    /**
     * 初始化字典树
     *
     * @param initStr 初始字符串集合
     */
    public void convertTrieTree(List<String> initStr) {
        for (String s : initStr) {
            TrieTree temp = this.root;
            char[] chars = s.toCharArray();
            for (char c : chars) {
                int index = (int) (c - 'a');
                if (temp.childArray[index] == null) {
                    TrieTree newNode = new TrieTree(c, 26);
                    temp.childArray[index] = newNode;
                }
                temp = temp.childArray[index];
            }
            temp.endingCharacter = true;
        }
    }

    /**
     * 字符串查找
     *
     * @param template 需要查找的字符串
     * @return 是否存在
     */
    public boolean find(String template) {
        TrieTree tmp = this.root;
        char[] chars = template.toCharArray();
        for (char c : chars) {
            int index = (int) (c - 'a');
            if (tmp.childArray[index] == null) return false;
            tmp = tmp.childArray[index];
        }
        return tmp.endingCharacter;
    }

    public static void main(String[] args) {
        TrieTreeDemo trieTree = new TrieTreeDemo();
        trieTree.convertTrieTree(Lists.newArrayList("hello", "hi", "how", "see", "so"));
        System.out.println(trieTree.find("hello"));
        System.out.println(trieTree.find("see"));
        System.out.println(trieTree.find("helloworld"));
    }

    static class TrieTree {
        private char c;
        private TrieTree[] childArray;
        private boolean endingCharacter = false;

        public TrieTree(int arrayLength, boolean endingCharacter) {
            this.childArray = new TrieTree[arrayLength];
            this.endingCharacter = endingCharacter;
        }

        public TrieTree(char c, int characterLength, boolean endingCharacter) {
            this.c = c;
            this.childArray = new TrieTree[characterLength];
            this.endingCharacter = endingCharacter;
        }

        public TrieTree(char c, int characterLength) {
            this.c = c;
            this.childArray = new TrieTree[characterLength];
        }

        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }

        public TrieTree[] getChildArray() {
            return childArray;
        }

        public void setChildArray(TrieTree[] childArray) {
            this.childArray = childArray;
        }

        @Override
        public String toString() {
            return "TrieTree{" +
                    "c=" + c +
                    ", childArray=" + Arrays.toString(childArray) +
                    ", endingCharacter=" + endingCharacter +
                    '}';
        }
    }
}
