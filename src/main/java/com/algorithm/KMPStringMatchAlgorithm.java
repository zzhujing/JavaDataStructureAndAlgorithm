package com.algorithm;

/**
 * kmp算法实现
 * 核心在于next[]的构建。
 * 第i个最长后缀的计算方式可以转化为迭代计算第i-1个次长后缀(最长后缀的最长后缀)，且次长索引后缀对应的模式串前缀索引位置(k)，满足template[k+1] = template[i]
 */
public class KMPStringMatchAlgorithm {

    /**
     * kmp算法
     *时间复杂度O(n)
     * @param main     主串
     * @param template 模式串
     */
    public static int kmp(String main, String template) {
        int n = main.length();
        int m = template.length();
        //1.构建预处理next数组(index->前缀索引下标，v->该前缀的最长后缀子串的结尾索引)
        int[] next = getNext(template);
        //2.定义坏字符位置
        int j = 0;
        //3.迭代i∈[0,n-1]
        for (int i = 0; i < n - 1; i++) {
            while (j > 0 && template.charAt(j) != main.charAt(i)) j = next[j - 1] + 1;
            if (main.charAt(i) == template.charAt(j)) ++j;
            if (j == m) return i - m + 1;
        }
        return -1;
    }

    /**
     * 构建next数组(失效函数)
     * 实现复杂度O(m)
     * @param template 模式串
     * @return next数组
     */
    private static int[] getNext(String template) {
        //init result container
        int m = template.length();
        int[] next = new int[m];
        next[0] = -1;
        //定义相对i之前的i-1的次子串索引
        int k = -1;
        for (int i = 1; i < m; i++) {
            //当i和k+1不同的时候，此时需要拿k的最长后缀去比对( next(k) )，直到 i 和 k+1相同。
            while (k != -1 && template.charAt(i) != template.charAt(k + 1)) k = next[k];
            //当i和k+1的模式串字符相同的时候说明其为最长后缀子串
            if (template.charAt(i) == template.charAt(k + 1)) k++;
            next[i] = k;
        }
        return next;
    }

    public static void main(String[] args) {
        String main = "abcdabcdancd";
        String template = "dan";
        System.out.println(kmp(main,template));
    }
}
