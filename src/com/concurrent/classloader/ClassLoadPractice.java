package com.concurrent.classloader;

/**
 * @author : hujing
 * @date : 2019/11/5
 */
public class ClassLoadPractice {

    private static ClassLoadPractice instance = new ClassLoadPractice();
    private static int x = 0;
    private static int y;

    public ClassLoadPractice() {
        x++;
        y++;
    }

    public static void main(String[] args) {
        // x = 0 , y = 1
        System.out.println(ClassLoadPractice.x);
        System.out.println(ClassLoadPractice.y);
    }
}
