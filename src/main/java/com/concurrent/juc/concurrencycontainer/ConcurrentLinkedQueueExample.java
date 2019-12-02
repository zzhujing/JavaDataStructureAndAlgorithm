package com.concurrent.juc.concurrencycontainer;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class ConcurrentLinkedQueueExample {
    public static void main(String[] args) {
        ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 1000000; i++) {
            queue.offer(System.nanoTime());
        }
        long start = System.currentTimeMillis();
        //12279
        //5
        while (!queue.isEmpty()) {
            queue.poll();
        }
        System.out.println(System.currentTimeMillis() - start);


        List<List<String>> list = Lists.newArrayList();
        //常规写法
        final List<String> result = list.stream().flatMap(List::stream).collect(Collectors.toList());
        //自定义Collector写法
        list.stream().collect(ArrayList::new, List::addAll, List::addAll);
    }
}
