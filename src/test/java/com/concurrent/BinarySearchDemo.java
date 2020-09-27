package com.concurrent;

/**
 * @author hujing
 * @date Create in 2020/9/11
 * 二分查找
 **/
public class BinarySearchDemo {

    public static void main(String[] args) {
        int[] source = new int[]{4, 5, 6, 7, 1, 2, 3};
        System.out.println(binarySearchFindFirstEq(source, 7)); //3
        System.out.println(binarySearchFindLastEq(source, 7)); // 6
        System.out.println(binarySearchFindFirstGe(source, 7)); // 3
        System.out.println(binarySearchFindLastLe(source, 7)); // 6
        System.out.println(binarySearchCycleOrderedArray(source, 7));
    }

    public static int binarySearch(int[] data, int k) {
        int low = 0;
        int high = data.length - 1;
        while (low <= high) {
            //计算mid
            int mid = low + ((high - low) >> 1);
            if (data[mid] == k) {
                return mid;
            } else if (data[mid] < k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    public static int binarySearchWithRecursive(int[] data, int k) {
        return binarySearchRecursiveTask(data, 0, data.length - 1, k);
    }

    public static int binarySearchRecursiveTask(int[] data, int low, int high, int k) {
        if (low > high) {
            return -1;
        }
        int mid = low + ((high - low) >>> 1);
        if (data[mid] == k) {
            return mid;
        } else if (data[mid] < k) {
            return binarySearchRecursiveTask(data, mid + 1, high, k);
        } else {
            return binarySearchRecursiveTask(data, low, mid - 1, k);
        }
    }

    //链表实现二分法的时间复杂度 -> 每次都要求取中间节点位置O(n)

    //二分法实现求取平方跟
    public static double sqrt(double k, int decimalPlace) {
        double low = 0.0;
        double high = k;
        //每次都取中间值的平方和k做比较，误差在位数之下即可
        while (low <= high) {
            double mid = low + ((high - low) / 2);
            if (Math.abs(mid * mid - k) <= getLimit(decimalPlace)) {
                return mid;
            } else if (mid * mid > k) {
                high = mid;
            } else {
                low = mid;
            }
        }
        return -1;
    }

    //二分法获取第一个相等的值
    public static int binarySearchFindFirstEq(int[] data, int k) {
        int low = 0;
        int high = data.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (data[mid] < k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (low < data.length && data[low] == k) return low;
        return -1;
    }

    //二分法查找最后一个相等的值
    public static int binarySearchFindLastEq(int[] data, int k) {
        int low = 0;
        int high = data.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (data[mid] <= k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (high >= 0 && data[high] == k) return high;
        return -1;
    }


    //二分法查找第一个大于相等k的值
    public static int binarySearchFindFirstGe(int[] data, int k) {
        int low = 0;
        int high = data.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (data[mid] < k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (low < data.length && data[low] >= k) return low;
        return -1;
    }

    //二分法查找最后一个小于相等k的值
    public static int binarySearchFindLastLe(int[] data, int k) {
        int low = 0;
        int high = data.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (data[mid] <= k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        if (high >= 0 && data[high] <= k) return high;
        return -1;
    }

    /**
     * 二分查找循环有序数组
     */
    public static int binarySearchCycleOrderedArray(int[] arr, int k) {
        int low = 0;
        int high = arr.length - 1;

        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (arr[mid] == k) {
                return mid;
            } else if (arr[mid] > k) {
                if (arr[mid] > arr[low]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else if (arr[mid] < k) {
                if (arr[mid] > arr[low]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        return -1;
    }


    private static double getLimit(double decimalPlace) {
        return 1 / (10 * (decimalPlace * 2));
    }
}
