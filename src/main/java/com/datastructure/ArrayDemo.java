package com.datastructure;

import java.util.*;

/**
 * @author : hujing
 * @date : 2019/10/21
 * 数组Demo, 数组底层的内存地址是连续的，所以支持快速随机访问，在Java中也就是继承了{@link java.util.RandomAccess}接口，
 * 该接口是一个标记接口，支持随机访问的List在遍历方面使用for循环会比使用迭代器快
 */
public class ArrayDemo {
    public static void main(String[] args) {

        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < 1000000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        //test ArrayList And LinkedList foreach speed

        foreach(arrayList);
        foreach(linkedList);
    }

    private static void foreach(List<Integer> source) {
        if (source instanceof RandomAccess) {
            System.out.println("RandomAccess");
        }
        long foreachStartTime = System.currentTimeMillis();
        //use fast random access
        for (int i = 0; i < source.size(); i++) {
            source.get(i);
        }
        System.out.println("foreach spend time : " + (System.currentTimeMillis() - foreachStartTime) + "ms");

        long iterStartTime = System.currentTimeMillis();

        //iterator access
        Iterator<Integer> iter = source.iterator();
        while (iter.hasNext()) {
            iter.next();
        }

        System.out.println("iter spend time : " + (System.currentTimeMillis() - iterStartTime) + "ms");
    }
}
