package com.datastructure;

import java.util.function.Consumer;

/**
 * 大顶堆demo
 * 1. 是一种根节点比子节点都大的完全二叉树
 * 2. 因为是完全二叉树，所以使用数组储存会更加节约内存
 */
public class HeapDemo {

    private int[] values;
    private int capacity;
    private int count;

    public HeapDemo(int capacity) {
        this.capacity = capacity;
        this.values = new int[capacity + 1];
    }

    public static void main(String[] args) {
        int[] source = {2, 5, 32, 12, 11, 43, 22};
        HeapDemo heap = new HeapDemo(10);
//        heap.buildHeap(source);
//        heap.foreach(System.out::println);
        heap.heapSort(source);
    }

    //堆排序
    //建堆 -> O(n)
    //swap(data,count--,1),自上而下堆化
    public void heapSort(int[] data) {
        buildHeap(data);
        while (count > 1) {
            swap(values, count--, 1);
            //堆化
            heapUp(values, count, 1);
        }
        for (int i = 0; i < data.length; i++) {
            System.out.println(values[i + 1]);
        }
    }

    //构建大顶堆
    //有两种方式，一种是直接逐个添加然后自下而上的进行上浮操作
    //另外一种是直接从原数组中的一个非叶子结点依次进行自上而下的堆化操作
    public void buildHeap(int[] arr) {
        if (arr == null) return;
        System.arraycopy(arr, 0, this.values, 1, arr.length);
        this.count = arr.length;
        int cur = count / 2;
        for (int i = cur; i > 0; i--) {
            heapUp(this.values, count, i);
        }
    }

    //插入元素
    //自下而上进行堆化
    public void insert(int data) {
        if (count + 1 > capacity) return;
        values[++count] = data;
        int child = count;
        int parent = child / 2;

        while (parent > 0 && values[parent] < values[child]) {
            swap(values, child, parent);
            child = parent;
            parent = parent / 2;
        }
    }

    //删除堆顶
    //将堆顶和队尾元素互换
    //删掉尾结点
    //从堆顶进行堆化
    public int removeTop() {
        //交换首位
        int result = values[1];
        swap(values, --count, 1);
        //开始堆化
        heapUp(values, count, 1);
        return result;
    }

    //自上而下进行堆化
    //O(logn)
    public void heapUp(int[] data, int n, int i) {
        while (true) {
            int maxPost = i;
            if (2 * i <= n && data[maxPost] < data[2 * i]) maxPost = 2 * i;
            if (2 * i + 1 <= n && data[maxPost] < data[2 * i + 1]) maxPost = 2 * i + 1;
            if (maxPost == i) break;
            swap(data, i, maxPost);
            i = maxPost;
        }
    }

    public void foreach(Consumer<Integer> consumer) {
        for (int i = 1; i <= count; i++) {
            consumer.accept(values[i]);
        }
    }

    public void swap(int[] arr, int left, int right) {
        int tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }

}

