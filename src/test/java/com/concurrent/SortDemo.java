package com.concurrent;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author hujing
 * @date Create in 2020/9/9
 * 排序算法实现
 **/
public class SortDemo {

    public static void main(String[] args) {
        int[] source = new int[]{2, 54, 33, 22, 15, 65, 113, 11, 2, 2, 5};
//        bubbleSort(source);
//        mergeSort(source);
        countSort(source);
        Arrays.stream(source).forEach(System.out::println);
    }

    //冒泡
    public static void bubbleSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            boolean flag = false;
            for (int j = 0; j < len - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    flag = true;
                    swap(arr, j, j + 1);
                }
            }
            if (!flag) {
                break;
            }
        }
    }

    //插入
    public static void insertSort(int[] arr) {
        int len = arr.length;
        //从未排序区域依次遍历
        for (int i = 1; i < len; i++) {
            //依次和已排序区域进行比较
            int tmp = arr[i];
            int j = i - 1;
            for (; j >= 0; j--) {
                if (tmp < arr[j]) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j + 1] = tmp;
        }
    }

    //选择排序，在未排序区间选择最小的放到已排序区域的末尾
    public static void selectSort(int[] arr) {
        int len = arr.length;
        for (int i = 0; i < len; i++) {
            int min = Integer.MAX_VALUE;
            int minIndex = 0;
            for (int j = i; j < len; j++) {
                if (arr[j] < min) {
                    min = arr[j];
                    minIndex = j;
                }
            }
            swap(arr, i, minIndex);
        }
    }

    //归并,分治思想，拆分最小单位然后进行有序合并，最后自然就是有序的
    public static void mergeSort(int[] data) {
        mergeTask(data, 0, data.length - 1);
    }

    public static void mergeTask(int[] data, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int mid = (begin + end) / 2;
        mergeTask(data, begin, mid);
        mergeTask(data, mid + 1, end);
        merge(data, begin, mid, end);
    }

    public static void merge(int[] data, int begin, int mid, int end) {
        int[] tmp = new int[end - begin + 1];
        int p = 0;
        int left = begin;
        int right = mid + 1;
        while (left <= mid && right <= end) {
            if (data[left] > data[right]) {
                tmp[p++] = data[right++];
            } else {
                tmp[p++] = data[left++];
            }
        }
        while (right <= end) {
            tmp[p++] = data[right++];
        }
        while (left <= mid) {
            tmp[p++] = data[left++];
        }

        for (int i = 0; i < p; i++) {
            data[begin++] = tmp[i];
        }
    }


    //快速排序
    public static void quickSort(int[] data) {
        quickSortTask(data, 0, data.length - 1);
    }

    private static void quickSortTask(int[] data, int begin, int end) {
        if (begin >= end) {
            return;
        }
        int pivot = getPivot(data, begin, end);
        quickSortTask(data, begin, pivot - 1);
        quickSortTask(data, pivot + 1, end);
    }

    //获取分区索引
    public static int getPivot(int[] data, int begin, int end) {
        int pivot = data[end]; //用最后一个元素作为默认pivot
        int sentinel = begin; //哨兵
        for (int i = begin; i < end; i++) {
            if (data[i] <= pivot) {
                swap(data, i, sentinel++);
            }
        }
        swap(data, end, sentinel);
        return sentinel;
    }

    //桶排序
    public static void bucketSort(int[] data) {
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            min = Math.min(min, data[i]);
            max = Math.max(max, data[i]);
        }
        int bucketNum = (int) Math.ceil(BigDecimal.valueOf(max).subtract(BigDecimal.valueOf(min)).divide(BigDecimal.TEN).doubleValue());
        int[][] buckets = new int[bucketNum][10];
        int[] indexes = new int[bucketNum];
        //填充桶
        for (int i = 0; i < data.length; i++) {
            int bucket = getBucketNum(data[i], min);
            System.out.println(bucket);
            buckets[bucket][indexes[bucket]++] = data[i];
        }

        //每个桶都进行快速排序
        for (int i = 0; i < buckets.length; i++) {
            quickSort(buckets[i]);
        }
        //回填数据
        int index = 0;
        for (int i = 0; i < bucketNum; i++) {
            for (int j = 0; j < buckets[i].length; j++) {
                if (buckets[i][j] != 0) {
                    data[index++] = buckets[i][j];
                }
            }
        }
    }

    //计数排序,维护总范围长度数组，并存入<=当前索引值的个数，从后向前遍历，获取对应索引值的位置，填入目标数组，并且索引值--
    public static void countSort(int[] data) {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
            max = Math.max(max, data[i]);
        }

        int[] tmp = new int[max + 1];

        for (int i = 0; i < data.length; i++) {
            tmp[data[i]]++;
        }

        for (int i = 1; i < tmp.length; i++) {
            tmp[i] += tmp[i - 1];
        }

        int[] result = new int[data.length];

        for (int i = data.length - 1; i >= 0; i--) {
            result[tmp[data[i]]-- - 1] = data[i];
        }
        for (int i = 0; i < data.length; i++) {
            data[i] = result[i];
        }

    }

    public static int getBucketCreateNum(int max, int min) {
        return (int) Math.ceil(BigDecimal.valueOf(max).subtract(BigDecimal.valueOf(min)).divide(BigDecimal.TEN).doubleValue());
    }

    public static int getBucketNum(int source, int min) {
        return (int) Math.floor((source - min) / 10);
    }


    public static void swap(int[] arr, int x, int y) {
        int tmp = arr[x];
        arr[x] = arr[y];
        arr[y] = tmp;
    }
}
