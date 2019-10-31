package com.datastructure;

/**
 * @author : hujing
 * @date : 2019/10/23
 * 后进先出 -> Stack
 */
public class StackDemo {
    public static void main(String[] args) {
//        StackDemo stack = new StackDemo(10);
//        stack.push(2);
//        stack.push(3);
//        stack.push(2);
//        stack.push(4);
//        System.out.println(stack.pop());
//        System.out.println(stack.pop());
//        System.out.println(stack.pop());
//        System.out.println(isValid(""));
    }

    private int[] data;
    private int capacity;
    private int size;

    public StackDemo(int capacity) {
        this.data = new int[capacity];
        this.capacity = capacity;
        this.size = 0;
    }
    public void push(int i) {
        data[size++] = i;
        if (size == capacity) {
            grow();
        }
    }
    public int pop() {
        return data[--size];
    }

    private void grow() {
        int[] dest = new int[capacity + capacity >>> 1];
        System.arraycopy(data, 0, dest, 0, capacity);
        data = dest;
    }
}



