package com.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * 递归：
 * 优点 ： 代码简洁易懂
 * 缺点 ： 数据量大的时候耗时高，空间复杂度高，递归深度过深容易堆栈溢出，重复计算等。
 */
public class RecursionDemo {
    public static void main(String[] args) {
        System.out.println(fn1(20));
        System.out.println(fn1WithoutRecursion(20));
    }

    private static int depth = 0;

    public static int fn(int n) {
        ++depth;
        if (depth > 1000) throw new IllegalArgumentException("stackoverflow");
        if (n == 1) return 1;
        return fn(n - 1) + 1;
    }

    public static int fnWithoutRecursion(int n) {
        if (n == 1) return 1;
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result += 1;
        }
        return result;
    }

    private static Map<Integer, Integer> tmpMap = new HashMap<>();

    public static int fn1(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;
        if (tmpMap.containsKey(n)) {
            return tmpMap.get(n);
        }
        depth++;
        int result = fn1(n - 1) + fn1(n - 2);
        return result;
    }

    public static int fn1WithoutRecursion(int n) {
        if (n==1) return 1;
        if (n==2) return 2;
        int result = 0;
        int pre = 2;
        int prepre = 1;
        for (int i = 3; i <=n ; i++) {
            result = pre + prepre;
            prepre = pre;
            pre = result;
        }
        return result;
    }
}
