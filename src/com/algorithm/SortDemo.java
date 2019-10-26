package com.algorithm;

import java.util.Arrays;

/**
 * 冒泡排序
 */
public class SortDemo {
    public static void main(String[] args) {
        int[] source = {2, 12, 4, 44, 3, 1, 3, 5};
//        selectSort(source, source.length);
        shellSort(source);
        System.out.println(Arrays.toString(source));
    }


    /**
     * 冒泡排序 ： 依次比较相邻的数据，大的交换到右边
     * 是原地排序，稳定排序
     * 时间复杂度 ： O(n^2) , 排好序的数组的无序度为 (n * (n-1) )/2 ,完全无序的为0，平均为(n * (n-1) )/4
     * 排序看做有无序变成有序的过程，所以交换的次数+比较的次数 = 排序时间 , 所以平均交换次数大约为排序的时间为O(n^2)
     *
     * @param arr    原数组
     * @param length 原数组长度
     */
    public static void bubbleSort(int[] arr, int length) {
        int swapCount = 0;
        for (int i = 0; i < length; i++) {
            for (int j = 1; j < length - i; j++) {
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                    swapCount++;
                }
            }
            if (swapCount == 0) break;
        }
    }

    public static void insertSort1(int[] arr, int length) {
        for (int i = 1; i < length; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[i] < arr[j]) {
                    //移动 j -> i
                    int tmp = arr[i];
                    System.arraycopy(arr, j, arr, j + 1, i - j);
                    arr[j] = tmp;
                    break;
                }
            }
        }
    }

    /**
     * 插入排序 : 在头部维护一个有序区域，依次从1 -> n的索引开始遍历并和有序区域进行比较，
     * 当遇到大于有序区域的一个数字的时候，完成赋值 ， 当小于的时候将该有序区域值像左移动一位
     * 时间复杂度为O(n^2)
     *
     * @param arr    原数组
     * @param length 数组长度
     */
    public static void insertSort2(int[] arr, int length) {
        for (int i = 1; i < length; i++) {
            int tmp = arr[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > tmp) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j + 1] = tmp;
        }
    }

    /**
     * 选择排序 ： 在左边维护有序区域，每次从无序区域选取最小值填充到有序区域末尾
     * 时间复杂度O(n^2) 非稳定原地排序
     * @param arr    原数组
     * @param length 数组长度
     */
    public static void selectSort(int[] arr, int length) {
        for (int i = 0; i < length - 1; i++) {
            int minValue = arr[i];
            int min = i;
            for (int j = i; j < length; j++) {
                if (arr[j] < minValue) {
                    minValue = arr[j];
                    min = j;
                }
            }
            swap(arr, i, min);
        }
    }

    public static void shellSort(int[] arr) {
        int length = arr.length;
        int temp;
        for (int step = length / 2; step >= 1; step /= 2) { //迭代步长
            for (int i = step; i < length; i++) {  //当step=1的时候就变成了插入排序
                temp = arr[i];
                int j = i - step;
                while (j >= 0 && arr[j] < temp) {
                    arr[j + step] = arr[j];
                    j -= step;
                }
                arr[j + step] = temp;
            }
        }
    }

    public static void swap(int[] arr, int left, int right) {
        int tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }

}
