package com.concurrent;

import java.util.Arrays;

/**
 * @author hujing
 * @date Create in 2020/9/23
 * 大顶堆实现
 **/
public class HeapDemo {

    public static void main(String[] args) {
        Heap heap = new Heap(10);
        heap.add(5);
        heap.add(3);
        heap.add(4);
        heap.add(7);
        heap.add(9);
        heap.add(1);
        heap.add(2);
        System.out.println(Arrays.toString(heap.data));
    }

    static class Heap {

        private final int[] data;
        private int size;

        Heap(int capacity) {
            data = new int[capacity + 1];
            size = 0;
        }

        Heap(int[] source) {
            build(source);
            data = source;
            size = source.length - 1;
        }

        //堆排序，将一个数组构建成堆
        private void build(int[] source) {
            //从最后一个非叶子节点向上进行堆化即可
            int size = source.length - 1;
            for (int i = size / 2; i >= 1; i--) {
                heapify(source, size, i);
            }
        }

        //堆排序
        public void sort() {
            //依次取出堆顶元素放置到末尾
            for (; ; ) {
                if (size == 0) return;
                int i = 1;
                swap(data, size--, 1);
                heapify(data, size, i);
            }
        }

        //添加,从下往上堆化
        public void add(int val) {
            if (size >= data.length - 1) {
                System.out.println("元素满了");
                return;
            }
            int i = ++size;
            data[i] = val;
            while (i / 2 >= 1 && data[i / 2] < val) {
                data[i] = data[i / 2];
                i = i / 2;
            }
            data[i] = val;
        }

        //交换结尾，从上往下堆化
        public int remove() {
            if (size == 0) {
                return -1;
            }
            int result = data[1];
            swap(data, size, 1);
            data[size--] = 0;
            //从上往下堆化
            heapify(data, size, 1);
            return result;
        }

        public void heapify(int[] data, int n, int i) {
            for (; ; ) {
                int maxPos = i;
                if (2 * i <= n && data[i] < data[i * 2]) maxPos = 2 * i;
                if (2 * i + 1 <= n && data[maxPos] < data[2 * i + 1]) maxPos = 2 * i + 1;
                if (maxPos == i) {
                    break;
                }
                swap(data, i, maxPos);
                i = maxPos;
            }
        }


        public void swap(int[] a, int x, int y) {
            int tmp = a[x];
            a[x] = a[y];
            a[y] = tmp;
        }
    }
}
