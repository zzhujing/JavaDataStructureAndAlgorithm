package com.algorithm;

import java.util.Stack;


/**
 * 使用栈实现一个最小栈
 * 要求：
 * 1. 一个获取栈中最小值的方法{@link #getMin()}
 * 2. 入栈方法 {@link #push(int)}
 * 3. 获取栈顶方法{@link #top()}
 * 4. 栈顶出栈方法{@link #pop()}
 */
public class MinStackDemo {

    private Stack<Integer> stack;

    /**
     * initialize your data structure here.
     */
    public MinStackDemo() {
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