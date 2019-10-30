package com.algorithm;

import java.util.Stack;

/**
 * 使用Stack校验对称括号
 */
public class StackCheckStrValidDemo {
    public static void main(String[] args) {
        System.out.println(isValid("([{])")); //false
        System.out.println(isValid("([{}])")); //true
    }

    /**
     * 判断是否是合法对应括号字符串
     */
    public static boolean isValid(String s) {
        if (s.isEmpty() || s.trim().isEmpty()) return true;
        char[] chars = s.toCharArray();
        if (chars.length % 2 != 0) {
            return false;
        }
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < chars.length; i++) {
            if (stack.size() == 0) {
                stack.push(chars[i]);
                //这里拿出栈顶元素和当前索引字符判断是否是配对的。是则栈顶出栈
            } else if (isSymmetry(stack.peek(), chars[i])) {
                stack.pop();
            } else {
                stack.push(chars[i]);
            }
        }
        //最后判断栈是否为空，为空说明所有的字符都匹配
        return stack.isEmpty();
    }

    public static boolean isSymmetry(char a, char b) {
        return (a == '(' && b == ')') || (a == '[' && b == ']') || (a == '{' && b == '}');
    }

}
