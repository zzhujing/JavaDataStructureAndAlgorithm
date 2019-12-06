package com.algorithm;

import java.math.BigDecimal;

public class BinarySearchDemo {
    public static void main(String[] args) {
        int[] arr = {4, 5, 6, 7, 1, 2, 3};
        final int index = findFromCyclicOrderArray(5, arr, 0, arr.length - 1);
        System.out.println("index -> :" +
                index + "-> value :" + arr[index]);
    }

    public static int simpleBinarySearch(int obj, int[] source) {
        int low = 0;
        int height = source.length - 1;
        int result = 0;
        int cyclicCount = 0;
        //must be <= not <
        while (low <= height) {
            cyclicCount++;
            // h + l may be overflow Integer.MAX_VALUE
//            int mid = (height + low) / 2;
            int mid = low + (height - low) >>> 1;
            if (obj == source[mid]) {
                result = mid;
                break;
            } else if (obj < source[mid])
                height = mid - 1;
            else
                low = mid + 1;
        }
        System.out.printf("spend times : %s\n", cyclicCount);
        return result;
    }

    public static int binarySearchWithRecurse(int obj, int[] source) {
        int low = 0;
        int height = source.length - 1;
        return binarySearchRecursive(low, height, source, obj);
    }

    private static int binarySearchRecursive(int low, int height, int[] source, int obj) {
        if (low > height)
            return -1;
        int mid = low + ((height - low) >>> 1);
        final int midValue = source[mid];
        if (obj == midValue)
            return mid;
        else if (obj < midValue)
            return binarySearchRecursive(low, mid - 1, source, obj);
        else
            return binarySearchRecursive(mid + 1, height, source, obj);
    }

    //使用二分查找求任意数字平方根
    public static double calculateSqrt(double number, int threshold) {
        double limit = Math.pow(10, -threshold);
        double low;
        double height;
        if (number > 1) {
            low = 1;
            height = number;
        } else {
            low = number;
            height = 1;
        }
        double result = 0d;
        while (height - low > limit) {
            result = low + (height - low) / 2;
            //may be overflow
            if (number < result * result) {
                height = result;
            } else if (number > result * result) {
                low = result;
            } else {
                return formatReturnValue(result, threshold);
            }
        }
        return formatReturnValue((height + low) / 2, threshold);
    }

    public static double formatReturnValue(double source, int threshold) {
        return BigDecimal.valueOf(source).setScale(threshold, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * @param first 是否是第一个
     * @param ge    是否大于
     */
    public static int findSameBinarySearch(int val, int[] source, boolean first, boolean ge) {
        int l = 0;
        int h = source.length - 1;
        while (l <= h) {
            int mid = l + ((h - l) >> 1);
            int midValue = source[mid];
            if (val == midValue) {
                //需要查看左边是否有相同元素
                int tmp = mid;
                while (source[first ? --tmp : ++tmp] == val) {
                    mid = tmp;
                }
                return mid;
            } else if (val < midValue)
                h = mid - 1;
            else l = mid + 1;
        }
        //若没有找到则返回最近的
        return ge ? source[l] : source[h];
    }

    public static int findFirstGeValue(int val, int[] source) {
        int l = 0;
        int h = source.length - 1;
        while (l <= h) {
            int mid = l + ((h - l) >> 1);
            int midValue = source[mid];
            //当mid索引的值大于等于val的时候
            if (val <= midValue) {
                //判断年前一个索引值是否小于val，是说明mid为第一个大于等于val的值
                if (mid == 0 || source[mid - 1] < val) return mid;
                else h = mid - 1;
            } else l = mid + 1;
        }
        return -1;
    }

    public static int findLastLeValue(int val, int[] source) {
        int l = 0;
        int h = source.length - 1;
        while (l <= h) {
            int mid = l + ((h - l) >> 1);
            int midValue = source[mid];
            //当mid索引的值大于等于val的时候
            if (val >= midValue) {
                if (mid == 0 || source[mid + 1] > val) return mid;
                else l = mid + 1;
            } else h = mid - 1;
        }
        return -1;
    }

    public static int findFirstEqValue(int val, int[] source) {
        int l = 0;
        int h = source.length - 1;
        while (l <= h) {
            int mid = l + ((h - l) >> 1);
            int midValue = source[mid];
            //这里无需判断mid-1是否等于val，在最后直接判断l是否等于val即可
            //同理在获取最后一个Value的时候。也不需判断直接判断h是否等于val即可
            if (midValue >= val) h = mid - 1;
            else l = mid + 1;
        }
        if (l < source.length - 1 && source[l] == val) return l;
        return -1;
    }

    public static int findLastEqValue(int val, int[] source) {
        int l = 0;
        int h = source.length - 1;
        while (l <= h) {
            int mid = l + ((h - l) >> 1);
            int midValue = source[mid];
            //同理在获取最后一个Value的时候。也不需判断直接判断h是否等于val即可
            if (midValue <= val) l = mid + 1;
            else h = mid - 1;
        }
        if (l < source.length - 1 && source[h] == val) return h;
        return -1;
    }

    /**
     * 从循环顺序数组中查找数据
     *
     * @param val    要查找的数据值
     * @param source 原数组
     * @param begin  开始索引
     * @param end    结束索引
     * @return 查找数据索引
     */
    public static int findFromCyclicOrderArray(int val, int[] source, int begin, int end) {
        int mid = begin + ((end - begin) >> 1);
        int headValue = source[begin];
        int midValue = source[mid];
        if (val == midValue) return mid;
        if (midValue > headValue) {
            // l -> mid 为有序 ， mid+1 -> tail为循环有序
            if (val < headValue || val > midValue) return findFromCyclicOrderArray(val, source, mid + 1, end);
            else return binarySearch(source, val, begin, mid - 1);
        } else {
            // l->mid 为循环有序 mid+1 -> tail 为顺序
            if (val < headValue || val > midValue) return binarySearch(source, val, mid + 1, end);
            return findFromCyclicOrderArray(val, source, begin, mid - 1);
        }
    }

    private static int binarySearch(int[] source, int val, int begin, int end) {
        int mid = begin + ((end - begin) >> 1);
        while (begin <= end) {
            if (val == source[mid])
                return mid;
            else if (val < source[mid])
                end = mid - 1;
            else begin = mid + 1;
        }
        return -1;
    }
}
