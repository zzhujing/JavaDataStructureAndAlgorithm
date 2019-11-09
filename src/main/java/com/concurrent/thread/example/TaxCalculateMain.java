package com.concurrent.thread.example;

/**
 * @author : hujing
 * @date : 2019/10/25
 * 计算税收
 */
public class TaxCalculateMain {
    public static void main(String[] args) {
        System.out.println(new TaxCalculate(1000d, 20000d, (s,b) -> b * 0.1 + s * 0.15).getTax());
    }
}
