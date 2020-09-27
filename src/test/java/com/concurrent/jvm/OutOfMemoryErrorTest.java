package com.concurrent.jvm;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 **/
public class OutOfMemoryErrorTest {
    public static void main(String[] args) {
        //outOfMemoryErrorDemo();
        metaSpaceDemo();
    }

    static String base = "string";

    //可以通过-XX:MetaspaceSize -XX:MaxMetaspaceSize指定元空间大小
    //因为永久代初始化大小比较困难。调小容易永久代移除，太大容器老年代溢出
    //字符串在永久代中容易出现内存溢出
    private static void metaSpaceDemo() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String str = base + base;
            base = str;
            list.add(str.intern());
        }
    }

    private static void outOfMemoryErrorDemo() {
        List<byte[]> result = Lists.newArrayList();
        while (true) {
            try {
                result.add(new byte[1024 * 1024]);
            } catch (OutOfMemoryError e) {
                System.out.println("堆栈溢出。。" + e);
            }
        }
    }
}
