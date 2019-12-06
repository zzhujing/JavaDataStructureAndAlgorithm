package com.algorithm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 基于散列表和双向链表完成一个LRU缓存 -> LinkedHashMap本身就是这么一种数据结构。每个插入的Entry数组都会被拼接到一个整体的双向链表中。并且可以通过构造方法设置访问顺序
 */
public class LRUDemo {

    private Map<String, Object> map = Maps.newHashMap();
    private LinkedList<String> list = Lists.newLinkedList();
    private static final int MAX = 3;


    public void addCache(String s) {
        //查看是否存在
        if (map.containsKey(s)) {
            //存在将其移动到链表末尾
            list.remove(s);
        } else {
            if (list.size() == MAX) {
                list.removeFirst();
            }
            map.put(s, new Object());
        }
        list.addLast(s);
    }

    public String remove() {
        final String s = list.removeFirst();
        map.remove(s);
        return s;
    }

    public static void main(String[] args) {
/*
        LRUDemo demo = new LRUDemo();
        demo.addCache("Hello");
        demo.addCache("World");
        demo.addCache("Hello");
        demo.addCache("Java");
        demo.addCache("GoLang");
        demo.map.keySet().forEach(System.out::println);
        System.out.println("=====");
        demo.list.forEach(System.out::println);
*/
        LinkedHashMap<String, Object> map = new LinkedHashMap<>(3, 0.75f, true);
        map.put("Hello", new Object());
        map.put("World", new Object());
        map.put("Java", new Object());
        map.put("Hello", new Object());
        map.keySet().forEach(System.out::println);
    }
}
