package com.concurrent.classloader;

import java.util.Random;

/**
 * @author : hujing
 * @date : 2019/11/5
 * <p>
 * ClassLoader加载流程
 * <p>
 * - 加载 ：将二进制字节码文件加载到内存中
 * - 链接
 * - 验证，验证字节码是否合法。(javap -v )
 * - 准备，将静态变量赋予初值
 * - 解析，将符号引用转化为直接引用（赋予引用对象内存地址）
 * - 初始化
 * - 给静态变量赋予初值
 * 类的初始化
 * 1. 直接new对象
 * 2. 读取或修改该类静态变量
 * 3. 读取或者修改父类/父接口的静态变量
 * 4. 反射
 * 5. 初始化子类的时候会初始化父类
 * 6. 启动类
 * <p>
 * ----
 * 1. 子类引用父类静态变量不会初始化自类
 * 2. 使用数组引用不会初始化
 * 3. 使用final修饰的常量不会初始化
 * 4. 使用final修改的复杂类型在编译阶段无法计算，需要初始化阶段初始化
 */
public class ClassInitDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        //反射
        Class.forName("com.concurrent.classloader.Child");

        //子类调用父类静态变量
//        System.out.println(Child.a);

        //复杂final类型会初始化
//        System.out.println(Child.x);

//        Child[] arr = new Child[10];
    }
}

class Child extends Obj {
    public static final int child = 1;
    public static final int x = new Random().nextInt(10);
    static {
        System.out.println("Child init");
    }
}

class Obj {
    public static int a = 1;

    static {
        System.out.println("Obj Init");
    }
}
