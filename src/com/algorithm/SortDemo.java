package com.algorithm;

import java.util.Arrays;

/**
 * 冒泡排序
 */
public class SortDemo {
    public static void main(String[] args) {
        int[] source = {2, 12, 4, 44, 3, 11, 3, 5,23,43};
//        selectSort(source, source.length);
        quickSort(source, source.length);
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
        //迭代轮数
        for (int i = 0; i < length; i++) {
            //无序区间依次遍历
            for (int j = 1; j < length - i; j++) {
                //移动最大值到末尾
                if (arr[j - 1] > arr[j]) {
                    swap(arr, j - 1, j);
                    swapCount++;
                }
            }
            //无交换直接结束排序
            if (swapCount == 0) break;
        }
    }


    /**
     * 选择排序 ： 在左边维护有序区域，每次从无序区域选取最小值填充到有序区域末尾
     * 时间复杂度O(n^2) 非稳定原地排序
     *
     * @param arr    原数组
     * @param length 数组长度
     */
    public static void selectSort(int[] arr, int length) {
        //迭代轮数
        for (int i = 0; i < length - 1; i++) {
            //记录最低值和其索引值
            int minValue = arr[i];
            int min = i;
            //依次遍历无序区间
            for (int j = i; j < length; j++) {
                if (arr[j] < minValue) {
                    minValue = arr[j];
                    min = j;
                }
            }
            //将最小值移动到有序区间末尾
            swap(arr, i, min);
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
    public static void insertSort(int[] arr, int length) {
        //迭代无序区间
        for (int i = 1; i < length; i++) {
            //记录当前排序值
            int tmp = arr[i];

            //遍历有序区间依次和当前排序值进行比较
            int j = i - 1;
            for (; j >= 0; j--) {

                //若当前有序区间值大于当前排序值则有序区间向前移动一位
                if (arr[j] > tmp) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            //最后将无序值插入对应索引处
            arr[j + 1] = tmp;
        }
    }


    /**
     * 希尔排序
     *
     * @param arr
     */
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

    /**
     * 归并排序
     * 时间复杂度: O(nlogn)
     * 空间复杂度: o(n)
     * 1. 获取中点分成两部分进行递归排序
     * 2. 合并两排序后的结果
     * 3. 递归结束条件为开始索引 <= 结束索引
     * <p>
     * T(n) = 2 * T(n/2) + n
     * T(n) = 2 * T(2 * T(n/4) + n/2) + n = 4 * T(n/4) + 2n
     * 第k次　－> T(n) = 2^k * T(n/2^k) + k*n
     * 当前n/2^k = 1 的时候划分结束 -> k = logn -> T(n) = nlongn + Cnn
     */
    public static void mergeSort(int[] arr, int length) {
        mergeSortRecursive(arr, 0, length);
    }

    private static void mergeSortRecursive(int[] arr, int begin, int end) {
        if (end <= begin) {
            return;
        }
        int mid = (begin + end) / 2;
        mergeSortRecursive(arr, begin, mid);
        mergeSortRecursive(arr, mid + 1, end);
        merge(arr, begin, mid, end);
    }

    private static void merge(int[] arr, int begin, int mid, int end) {
        int leftBegin = begin;
        int rightBegin = mid + 1;
        int index = 0;
        int[] tmp = new int[end - begin + 1];
        while (leftBegin <= mid && rightBegin <= end) {
            if (arr[leftBegin] <= arr[rightBegin]) {
                tmp[index++] = arr[leftBegin++];
            } else {
                tmp[index++] = arr[rightBegin++];
            }
        }

        while (leftBegin <= mid) {
            tmp[index++] = arr[leftBegin++];
        }

        while (rightBegin <= end) {
            tmp[index++] = arr[rightBegin++];
        }

        for (int i = 0; i < tmp.length; i++) {
            arr[begin + i] = tmp[i];
        }
    }


    /**
     * 快速排序
     * 1. 根据标志点分区分为大于标志点和小于标志点
     * 2. 对两区域继续递归进行分区
     * 3. 递归结束完成
     */
    public static void quickSort(int[] arr, int length) {
        quickSortRecursive(arr, 0, length - 1);
    }

    private static void quickSortRecursive(int[] arr, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int pivot = partition(arr, begin, end);
        quickSortRecursive(arr, begin, pivot - 1);
        quickSortRecursive(arr, pivot + 1, end);
    }

    private static int partition(int[] arr, int begin, int end) {
        //use end index for init pivot
        int pivot = arr[end];
        int lastLessThanPivotValueIndex = begin;
        for (int i = begin; i < end; i++) {
            //如果比标志点小，那么互换i和lastLessThanPivotValueIndex的值，并且lastLessThanPivotValueIndex++
            if (arr[i] <= pivot) {
                swap(arr, lastLessThanPivotValueIndex++, i);
            }
        }
        swap(arr, lastLessThanPivotValueIndex, end);
        return lastLessThanPivotValueIndex;
    }


    public static void swap(int[] arr, int left, int right) {
        int tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }

}
