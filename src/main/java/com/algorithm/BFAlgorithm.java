package com.algorithm;

/**
 * @author hujing
 * @date Create in 2020/9/27
 * Brute Force String Match Algorithm
 * 暴力匹配 模式串长度m 主串长度n 时间复杂度O (n-m+1) * m
 **/
public class BFAlgorithm {

    public static void main(String[] args) {
        System.out.println(indexOf("Hello", "el"));
    }

    public static int indexOf(String n, String m) {
        //模式串超过主串不可能存在
        if (m.length() > n.length()) {
            return -1;
        }
        char[] mainChars = n.toCharArray();
        char[] templateChars = m.toCharArray();
        int threshold = mainChars.length - templateChars.length + 1;
        for (int i = 0; i < threshold; i++) { //在n中选择m有n-m+1种情况
            boolean matched = true;
            for (int j = 0; j < templateChars.length; j++) { //依次比较模式串长度字符
                if (templateChars[j] != mainChars[i + j]) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return i;
            }
        }
        return -1;
    }
}
