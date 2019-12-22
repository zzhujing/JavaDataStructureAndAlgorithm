package com.datastructure;

import java.util.Random;

/**
 * 自定义简易跳表
 * 1. 添加的时候需要通过{@link SimpleSkipList#findWithHead(Integer source)} 来获取要添加值的附近节点
 * 2. 插入
 * 3. 通过随机算法{@link SimpleSkipList#random}来判断是否需要添加层级
 * 4. 添加层级需要获取到附近节点的上层节点
 *     while(tmp.value!=null && tmp.up ==null){
 *         tmp = tmp.left;
 *     }
 * 5. 插入
 *
 * 6. 删除方法，
 *  - 通过{@link #contains(Integer)}方法获取到目标值的节点
 *  - 删除左右关系
 *  - 如果有up节点也删除上下关系
 *  - 最后如果层级大于1并且只有head和tail的需要删掉
 *   while(head.right = tail && height > 0){
 *       head = head.down;
 *       tail = tail.down;
 *   }
 *
 */
public class SimpleSkipList {

    //节点类型，区分节点
    private static final byte HEAD_TYPE = (byte) -1;
    private static final byte TAIL_TYPE = (byte) 1;
    private static final byte DATA_TYPE = (byte) 0;
    private Node head;
    private Node tail;
    private int size;
    private int height;
    private Random random; //随机算法

    public SimpleSkipList() {
        this.head = new Node(null, HEAD_TYPE);
        this.tail = new Node(null, TAIL_TYPE);
        head.right = tail;
        tail.left = head;
        this.random = new Random(System.currentTimeMillis());
    }

    /**
     * 根据要添加的值去跳表中寻找最近的left元素
     */
    public Node findWithHead(Integer source) {
        Node tmp = head;
        for (; ; ) {
            //当右边元素不为tail且元素值小于等于source，那么移动当前指针位置到right节点
            while (tmp.right.type != TAIL_TYPE && tmp.right.value <= source) {
                tmp = tmp.right;
            }
            //判断是否属于第一层
            if (tmp.down != null) tmp = tmp.down;
                //是第一层结束循环
            else break;
        }
        return tmp;
    }

    /**
     * 从后往前找
     */
    public Node findWithTail(Integer v) {
        Node tmp = tail;
        while (true) {
            while (tmp.left.type != HEAD_TYPE && tmp.left.value > v) {
                tmp = tmp.left;
            }
            if (tmp.down != null) tmp = tmp.down;
            else break;
        }
        return tmp;
    }

    public void addWithTail(Integer v) {
        Node nearNode = findWithTail(v);
        Node newNode = new Node(v);
        newNode.left = nearNode.left;
        newNode.right = nearNode;
        nearNode.left.right = newNode;
        nearNode.left = newNode;
        int currentLevel = 0;
        while (random.nextDouble() < 0.5d) {
            if (currentLevel >= height) {
                height++;
                Node newLevelHead = new Node(null, HEAD_TYPE);
                Node newLevelTail = new Node(null, TAIL_TYPE);
                newLevelHead.right = newLevelTail;
                newLevelTail.left = newLevelHead;
                newLevelHead.down = head;
                newLevelTail.down = tail;
                head.up = newLevelHead;
                tail.up = newLevelTail;
                head = newLevelHead;
                tail = newLevelTail;
            }

            //查询上层元素
            while (nearNode.value != null && nearNode.up == null) {
                nearNode = nearNode.right;
            }
            nearNode = nearNode.up;
            Node upNode = new Node(v);
            upNode.left = nearNode.left;
            upNode.right = nearNode;
            upNode.down = newNode;
            nearNode.left.right = upNode;
            nearNode.left = upNode;
            newNode.up = upNode;
            newNode = upNode;
            currentLevel++;
        }
        size++;
    }

    public void addWithHead(Integer source) {
        //查询出附近元素
        Node nearNode = this.findWithHead(source);
        Node newNode = new Node(source);
        newNode.left = nearNode;
        newNode.right = nearNode.right;
        nearNode.right.left = newNode;
        nearNode.right = newNode;
        int currentLevel = 0;
        //随机
        while (random.nextDouble() < 0.5d) {
            //如果超越层高，则构建head tail关系
            if (currentLevel >= height) {
                Node newLevelHead = new Node(null, HEAD_TYPE);
                Node newLevelTail = new Node(null, TAIL_TYPE);
                newLevelHead.right = newLevelTail;
                newLevelTail.left = newLevelHead;
                newLevelHead.down = head;
                newLevelTail.down = tail;
                tail.up = newLevelTail;
                head.up = newLevelHead;
                head = newLevelHead;
                tail = newLevelTail;
                height++;
            }
            //查询newNode的上层元素
            while (nearNode.value != null && nearNode.up == null) {
                nearNode = nearNode.left;
            }
            nearNode = nearNode.up;
            Node upNode = new Node(source);

            upNode.right = nearNode.right;
            upNode.left = nearNode;
            upNode.down = newNode;
            nearNode.right.left = upNode;
            nearNode.right = upNode;
            newNode.up = upNode;
            newNode = upNode;
            currentLevel++;
        }
        size++;
    }

    public Integer remove(Integer val) {
        final Node node = findWithHead(val);
        if (!node.value.equals(val)) {
            return null;
        } else {
            //遍历删除node及其up节点
            Node tmp = node;
            Node up = null;
            while (tmp.left != null) {
                //删除tmp
                tmp.left.right = tmp.right;
                tmp.right.left = tmp.left;
                tmp.left = null;
                tmp.right = null;
                if (tmp.up != null) {
                    up = tmp.up;
                    tmp.up.down = null;
                    tmp.up = null;
                    tmp = up;
                }
            }
            //消除空于的层级
            while (head.right == tail && height > 0) {
                head = head.down;
                tail = tail.down;
            }
            return val;
        }
    }


    public void dumpSkipList() {
        Node tmp = head;
        int curHeight = height + 1;
        while (tmp != null) {
            System.out.printf("total height [%d] , current level [%d]", height + 1, curHeight--);
            Node cur = tmp.right;
            System.out.print(" data :[");
            while (cur.type == DATA_TYPE) {
                System.out.printf(" %d", cur.value);
                cur = cur.right;
            }
            tmp = tmp.down;
            System.out.print(" ]\n");
        }
        System.out.println("--------------");
    }

    public boolean contains(Integer source) {
        final Node result = findWithHead(source);
        return result.value.equals(source);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public Integer get(Integer source) {
        return contains(source) ? source : null;
    }

    public static class Node {
        private Integer value;
        private byte type;
        private Node up, down, left, right;

        public Node(Integer value, byte type) {
            this.value = value;
            this.type = type;
        }

        public Node(Integer value) {
            this(value, DATA_TYPE);
        }
    }

    public static void main(String[] args) {
        SimpleSkipList skipList = new SimpleSkipList();
        skipList.addWithTail(11);
        skipList.addWithTail(12);
        skipList.addWithTail(22);
        skipList.addWithTail(33);
        skipList.addWithTail(44);
        skipList.addWithTail(13);
        skipList.addWithTail(55);
        skipList.addWithTail(16);
        skipList.dumpSkipList();
        System.out.println(skipList.remove(11));
        System.out.println(skipList.remove(13));
        System.out.println(skipList.remove(22));
        System.out.println(skipList.remove(33));
        skipList.dumpSkipList();
    }
}
