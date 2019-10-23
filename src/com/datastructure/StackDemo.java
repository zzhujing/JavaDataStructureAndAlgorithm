package com.datastructure;

import java.util.*;

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

    public boolean isValid(String s) {
        if (s.isEmpty() || s.trim().isEmpty()) return true;
        char[] chars = s.toCharArray();
        if (chars.length % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < chars.length; i++) {
            if (stack.size() == 0) {
                stack.push(chars[i]);
            } else if (isSymmetry(stack.peek(), chars[i])) {
                stack.pop();
            } else {
                stack.push(chars[i]);
            }
        }
        return stack.isEmpty();
    }

    public boolean isSymmetry(char a, char b) {
        return (a == '(' && b == ')') || (a == '[' && b == ']') || (a == '{' && b == '}');
    }
}


class MinStack {

    private Stack<Integer> stack;

    /**
     * initialize your data structure here.
     */
    public MinStack() {
        stack = new Stack<>();
    }

    public void push(int x) {
        if (stack.isEmpty()) {
            stack.push(x);
            stack.push(x);
        } else {
            Integer peek = stack.peek();
            stack.push(x);
            if (peek < x) {
                stack.push(peek);
            } else {
                stack.push(x);
            }
        }
    }

    public void pop() {
        stack.pop();
        stack.pop();
    }

    public int top() {
        return stack.get(stack.size() - 2);
    }

    public int getMin() {
        return stack.peek();
    }
}
