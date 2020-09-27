package com.algorithm;

/**
 * @author hujing
 * @date Create in 2020/9/27
 * Pk 字符串匹配算法 (巧妙使用hash进制算法连接相邻的模式串降低计算每个模式串的复杂度)
 **/
public class PKAlgorithm {


    public static void main(String[] args) {
        System.out.println(indexOf("hello", "Hello"));
    }

    /**
     * 对于BF的匹配的时间复杂度过高，可以使用hash算法对 n-m+1个模式串进行转化成26进制(针对具体字符的范围)，
     * 此时第i个和第i+1个模式串之间hash值的关系为:
     * hash(i) =                      26^0(n(i)-'a') + 26^1(n(i+1)-'a') + .... + 26^(m-1)(n(i+m-1)-'a')
     * hash(i-1) = 26^0(n(i-1)-'a') + 26^1(n(i)-'a') + .... + 26^(m-1)(n(i+m-2)-'a')
     * <p>
     * => hash(i) = ( hash(i-1) - 26^0(n(i-1)-'a') ) / 26 + 26^(m-1)(n(i+m-1)-'a')
     *
     * @param n 主串
     * @param m 模式串
     * @return 模式串在主串中的第一个字符的索引位置
     */
    public static int indexOf(String n, String m) {
        if (m.length() > n.length()) {
            return -1;
        }
        //求m的hash值
        int hash = 0;
        for (int i = 0; i < m.length(); i++) {
            hash += Math.pow(26, i) * (m.charAt(i) - 'a');
        }

        int prev = -1;
        int cur;
        //计算每个模式串的hash值
        for (int i = 0; i < n.length() - m.length() + 1; i++) {
            if (prev == -1) {
                prev = calcHash(i, m.length(), n);
                cur = prev;
            } else {
                cur = calcCurHash(prev, i - 1, m.length(), n);
                prev = cur;
            }
            if (cur == hash) {
                return i;
            }
        }
        return -1;
    }

    public static int calcCurHash(int prevHash, int prevIndex, int length, String baseStr) {
        return (int) ((prevHash - baseStr.charAt(prevIndex) + 'a') / 26 + Math.pow(26, length - 1) * (baseStr.charAt(prevIndex + length) - 'a'));
    }

    /**
     * 获取某字符串的hash值
     *
     * @param begin   开始索引
     * @param length  模式串长度
     * @param baseStr 基础字符串
     * @return hash
     */
    public static int calcHash(int begin, int length, String baseStr) {
        int hash = 0;
        for (int i = 0; i < length; i++) {
            hash += Math.pow(26, i) * (baseStr.charAt(begin + i) - 'a');
        }
        return hash;
    }
}
