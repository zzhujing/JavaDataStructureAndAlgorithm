package com.concurrent.juc.concurrencycontainer;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 测试SkipListMap的方法
 */
public class ConcurrentSkipListMapExample {
    public static void main(String[] args) {
        ConcurrentSkipListMap<Integer, String> skipListMap = new ConcurrentSkipListMap<>();
        skipListMap.put(1, "Java");
        skipListMap.put(10, "GoLang");
        skipListMap.put(50, "JavaScript");
        System.out.println(skipListMap.compute(2, (k, v) -> k + v)); //指定key为null会抛空指针
        System.out.println(skipListMap.get(1));

        skipListMap.computeIfAbsent(2, k -> "Python"); //如果不存在则k，则将其经过该Function#apply之后 put()
        System.out.println(skipListMap.get(2));

        skipListMap.computeIfPresent(2, (k, ov) -> "Java"); //如果存在则重新进行修改并put()
        System.out.println(skipListMap.get(2));

        skipListMap.merge(1, "Kotlin", (ov, v) -> "PHP"); //如果存在则经过BiFunction#apply ，最后put into Map
        System.out.println(skipListMap.get(1));

        System.out.println(skipListMap.ceilingEntry(2));
        System.out.println(skipListMap.ceilingKey(2));

        System.out.println(skipListMap.floorEntry(10));
        System.out.println(skipListMap.floorKey(10));

        System.out.println("======");
        final ConcurrentNavigableMap<Integer, String> navigableMap = skipListMap.headMap(3, true);
        navigableMap.forEach((k, v) -> System.out.println(k + " , " + v));
        navigableMap.put(2, "C++");
        navigableMap.forEach((k, v) -> System.out.println(k + " , " + v));
        System.out.println("=======");
        skipListMap.forEach((k, v) -> System.out.println(k + " , " + v));

    }
}
