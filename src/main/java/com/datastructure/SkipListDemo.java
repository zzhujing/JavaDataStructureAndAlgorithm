package com.datastructure;

import static com.datastructure.SkipListDemo.SkipList.MAX_LEVEL;

/**
 * @author hujing
 * @date Create in 2020/9/12
 **/
public class SkipListDemo {

    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.insert(1);
        skipList.insert(3);
        skipList.insert(5);
        skipList.insert(2);
        skipList.insert(4);
        skipList.insert(7);
        skipList.print();
    }


    static class SkipList {
        public static final int MAX_LEVEL = 16;
        public static final float RANDOM_SEED = 0.5f;
        private Node head = new Node();
        private int levelCount;

        public Node find(int value) {
            Node p = head;
            for (int i = levelCount; i >= 0; i--) {
                while (p.forwards[i] != null && p.forwards[i].data < value) {
                    p = p.forwards[i];
                }
            }

            if (p.forwards[0] != null && p.forwards[0].data == value) {
                return p.forwards[0];
            }
            return null;
        }

        public void insert(int value) {
            int level = randomLevel();
            Node newNode = new Node();
            newNode.data = value;
            newNode.levelCount = level;
            Node[] update = new Node[level];

            //查询要插入的节点位置
            Node p = head;
            for (int i = level - 1; i >= 0; i--) {
                while (p.forwards[i] != null && p.forwards[i].data < value) {
                    p = p.forwards[i];
                }
                update[i] = p;
            }

            //插入
            for (int i = level - 1; i >= 0; i--) {
                newNode.forwards[i] = update[i].forwards[i];
                update[i].forwards[i] = newNode;
            }

            if (level > levelCount) levelCount = level;
        }

        public void delete(int value) {
            Node[] update = new Node[levelCount];
            Node p = head;
            for (int i = levelCount - 1; i >= 0; i--) {
                while (p.forwards[i] != null && p.forwards[i].data < value) {
                    p = p.forwards[i];
                }
                update[i] = p;
            }
            if (p.forwards[0] != null && p.forwards[0].data == value) {
                //找到删除元素
                for (int i = levelCount - 1; i >= 0; i--) {
                    if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
                        update[i].forwards[i] = update[i].forwards[i].forwards[i];
                    }
                }
            }

            while (levelCount > 1 && head.forwards[levelCount] == null) levelCount--;
        }

        public void print() {
            Node p = head;
            StringBuilder builder = new StringBuilder();
            for (int i = levelCount - 1; i >= 0; i--) {
                while (p.forwards[i] != null) {
                    builder.append(p.forwards[i].data).append(" ");
                    p = p.forwards[i];
                }
                p = head;
                builder.append("\n");
            }
            System.out.println(builder.toString());
        }


        //随机添加固定层数算法
        // 0.5 -> +1
        // 0.25 -> +2
        // 0.125 -> +3
        public int randomLevel() {
            int level = 1;
            while (Math.random() < RANDOM_SEED && level < MAX_LEVEL) {
                level += 1;
            }
            return level;
        }


    }

    static class Node {
        private int data = -1;
        private Node forwards[] = new Node[MAX_LEVEL]; //所有层级的next节点
        private int levelCount;
    }
}
