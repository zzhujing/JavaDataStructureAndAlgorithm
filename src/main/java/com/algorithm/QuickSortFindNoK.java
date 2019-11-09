package com.algorithm;

/**
 * @author : hujing
 * @date : 2019/10/28
 * 使用快排思想在O(n)内查看一个无序列表的第K大值
 * 1.　按基准点拆分为区域
 * 2. 判断k所在区间,并在指定区间进行递归迭代直至pivot+1 = k (这里+1是因为返回的pivot是索引下标)
 */
public class QuickSortFindNoK {

    public static void main(String[] args) {
        int[] arr = {1, 3, 4, 2, 123, 43, 32, 55};
        System.out.println(findNoK(arr, 0, arr.length - 1, 8));
    }

    public static int findNoK(int[] arr, int begin, int end, int k) {

        if (k > arr.length) throw new IllegalArgumentException("超出索引位置");

        int pivot = getPivot(arr, begin, end);
        //如果基准点的索引=1为第K个数则直接返回
        if (pivot + 1 == k) return arr[pivot];

            //大于的话到较大区间继续递归
        else if (pivot + 1 > k) return findNoK(arr, begin, pivot - 1, k);

            //小于的话到较小区间
        else return findNoK(arr, pivot + 1, end, k);
    }

    /**
     * 快排思想获取中间基准点索引位置
     */
    private static int getPivot(int[] arr, int begin, int end) {
        int pivot = arr[end];
        int lastLessThanPivotValueIndex = begin;
        for (int i = begin; i < end; i++) {
            if (arr[i] <= pivot) {
                SortDemo.swap(arr, lastLessThanPivotValueIndex++, i);
            }
        }
        SortDemo.swap(arr, lastLessThanPivotValueIndex, end);
        return lastLessThanPivotValueIndex;
    }
}
