package com.algorithm;

import java.util.stream.IntStream;

/**
 * @author : hujing
 * @date : 2019/10/30
 * 斐波那契数列兔子问题
 */
public class RabbitTestDemo {
    public static void main(String[] args) {
        System.out.println(getRabbitNumberAfterNOnMonth(30));
        System.out.println(getRabbitNumberAfterNOnMonthWithRecursive(30));
    }

    /**
     * 时间复杂度 O(n) 空间复杂度O(n)
     */
    public static int getRabbitNumberAfterNOnMonth(int n) {
        int[] result = new int[n];
        IntStream.range(0, n)
                .forEach(i -> {
                    if (i == 0 || i == 1) {
                        result[i] = 1;
                    } else {
                        result[i] = result[i - 1] + result[i - 2];
                    }
                });
        return result[n - 1];
    }


    /**
     * 时间复杂度O(2^n) , 空间复杂度O(1),可以使用空间换时间将已经出现的数列存入到map中，就可以避免大量的重复计算
     */
    public static int getRabbitNumberAfterNOnMonthWithRecursive(int n) {
        if (n == 1 || n == 2) return 1;
        return getRabbitNumberAfterNOnMonthWithRecursive(n - 1) + getRabbitNumberAfterNOnMonthWithRecursive(n - 2);
    }
}
