package com.datastructure;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 堆数据结构的一些实际使用场景
 */
public class HeapInAction {

    private String[] data;
    private int count;
    private int capacity;

    public HeapInAction(int capacity) {
        this.capacity = capacity;
        this.data = new String[capacity + 1];
    }


    //构建堆
    //从最后一个非子叶节点开始从上往下进行堆化
    public void buildHeap(String[] arr) {
        if (arr.length == 0 || arr.length > capacity) return;
        System.arraycopy(arr, 0, this.data, 1, arr.length);
        this.count = arr.length;
        for (int i = count / 2; i > 0; i--) {
            heapify(data, count, i);
        }
    }

    //插入
    public void insert(String val) {
        if (count + 1 > capacity) return;
        data[++count] = val;
        int i = count;
        while (i / 2 > 0 && data[i].compareTo(data[i / 2]) < 0) {
            swap(data, i, i / 2);
            i = i / 2;
        }
    }

    //移除堆顶元素
    //交换堆的首尾元素
    //从上到下依次进行堆化
    public String removeTop() {
        String result = data[count];
        if (count == 0) return null;
        swap(data, count--, 1);
        heapify(data, count, 1);
        return result;
    }

    private void heapify(String[] data, int count, int cur) {
        while (true) {
            int minPost = cur;
            if (cur * 2 <= count && data[minPost].compareTo(data[cur * 2]) > 0) minPost = cur * 2;
            if (cur * 2 + 1 <= count && data[minPost].compareTo(data[cur * 2 + 1]) > 0) minPost = cur * 2 + 1;
            if (minPost == cur) break;
            swap(data, cur, minPost);
            cur = minPost;
        }
    }

    private void foreach() {
        Arrays.stream(data).skip(1).limit(count).forEach(System.out::println);
    }

    public void swap(String[] arr, int left, int right) {
        String tmp = arr[left];
        arr[left] = arr[right];
        arr[right] = tmp;
    }


    public static void findTopK(String[] arr, int k) {
        HeapInAction heap = new HeapInAction(k);
        heap.buildHeap(Arrays.copyOf(arr, k));
        for (int i = k; i < arr.length; i++) {
            if (heap.data[1].compareTo(arr[i]) < 0) {
                heap.removeTop();
                heap.insert(arr[i]);
            }
        }
        heap.foreach();
    }

    public static void main(String[] args) {
        findTopK(new String[]{"a","b","d","e","c","f","h","g"},2);
    }

    static class Data {
        private String data;
        private String no;

        public Data(String data, String no) {
            this.data = data;
            this.no = no;
        }
    }

    static class FileLineRead {
        private String filePath;
        private BufferedReader br;
        private LinkedList<Data> container;

        public FileLineRead(String filePath, LinkedList<Data> container) throws FileNotFoundException {
            this.filePath = filePath;
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            this.container = container;
        }

        public void readLine() throws IOException {
            String line = null;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            final char[] chars = builder.toString().trim().toCharArray();
            for (char c : chars) {
                container.addLast(new Data(String.valueOf(c), filePath.substring(0, 1)));
            }
        }

    }
}
