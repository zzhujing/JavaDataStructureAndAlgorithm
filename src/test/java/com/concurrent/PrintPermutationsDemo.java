package com.concurrent;

/**
 * @author hujing
 * @date Create in 2020/9/23
 * 打印全排列Demo,递归树查看时间复杂度 -> 阶乘
 **/
public class PrintPermutationsDemo {

    public static void main(String[] args) {
        int[] source = {1, 2, 3, 4};
        printPermutations(source, source.length, source.length);
    }

    /**
     * 打印全排列
     *
     * @param data 打印数组
     * @param n
     * @param k
     */
    public static void printPermutations(int[] data, int n, int k) {
        //当只剩 1个需要判断的时候结束递归
        if (k == 1) {
            for (int i = 0; i < n; ++i) {
                System.out.print(data[i] + " ");
            }
            System.out.println();
        }
        for (int i = 0; i < k; ++i) {
            //确定最后一个值
            int tmp = data[i];
            data[i] = data[k - 1];
            data[k - 1] = tmp;
            //从后往前依次确定值
            printPermutations(data, n, k - 1);
            //recovery
            tmp = data[i];
            data[i] = data[k - 1];
            data[k - 1] = tmp;
        }
    }

}
