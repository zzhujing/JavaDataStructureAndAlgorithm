package com.algorithm;

/**
 * BM字符串查找算法。
 */
public class BMAlgorithm {


    //模式串最大范围
    private static final int SIZE = 256;

    //根据模式串生初始化散列表
    private void generateHashtable(char[] templateChar, int length, int[] hashTable) {
        for (int i = 0; i < SIZE; i++) {
            hashTable[i] = -1;
        }
        for (int i = 0; i < length; i++) {
            // key -> 模式串字符 value -> 索引位置
            hashTable[templateChar[i]] = i;
        }
    }

    /**
     * 生成模式串的所有子串的关系
     *
     * @param b      模式串char[]
     * @param m      char[]长度
     * @param prev   prev[子串长度] = 子串是否是模式串的前缀子串
     * @param suffix index->子串长度 value->开始index
     */
    private void generateAllChildStrRelation(char[] b, int m, boolean[] prev, int[] suffix) {
        //1.初始化prev, suffix
        for (int i = 0; i < m; i++) {
            prev[i] = false;
            suffix[i] = -1;
        }
        //2.i∈[0,m-2]，依次和 b 进行后缀比对，然后填充prev,suffix
        for (int i = 0; i < m - 1; i++) {
            //初始化索引位置
            int j = i;
            //子串长度
            int l = 0;
            //迭代获取对应长度子串的初始位置(这里非常巧妙，i从0->m-2，过程若和模式串字符相同则会累加长度，并赋予初始索引位置。且会被后面的赋值给覆盖。)
            while (j >= 0 && b[j] == b[m - 1 - l]) {
                suffix[++l] = j--;
            }
            //若到达索引0说明为模式串前缀节点。修改状态为true。
            if (j == -1) prev[l] = true;
        }
    }


    /**
     * BM字符串匹配规则。
     * 坏字符匹配规则(Bad Character Rule 可能出现逆向或者不移动。) -> 使用一个数组来储存 （key-相对来说 最后出现的字符 value->index）
     * - 依次移动若匹配直接返回模式串对应在主串的初始位置索引
     * - 若不匹配则从后向前再主串中获取到第一个不匹配的字符，记录当前对应的模式串索引（Si)，然后在模式串中获取其位置(Xi)
     * - 若无则直接将初始位置索引移动到初始位置+模板串长度位置(Xi = -1)
     * - 若有则移动坏字符在模板中的位置-该字符所在位置(Xi = index)
     * - 移动(Si-Xi)
     * 好后缀匹配规则 -> 使用一个suffix数组来储存模式串的所有后缀子串的长度和开始索引对应关系以及是否匹配
     * - 不匹配则获取模式串中的好字符串后缀
     * - 在模式串中查找好字符串后缀的最长子串
     * - 若没有找到则移动初始值到当前模式串的后面
     * - 若找到则将匹配的后缀子串匹配对齐。
     */
    public int bm(String mainStr, String templateStr) {
        //1.获取主串char[] main 长度n,模式串templateChar[] 长度m
        char[] main = mainStr.toCharArray();
        char[] template = templateStr.toCharArray();
        int n = main.length;
        int m = template.length;
        int beginIndex = 0;
        // 初始化散列表
        int[] hashTable = new int[SIZE];
        generateHashtable(template, template.length, hashTable);
        //初始化好后缀
        boolean[] prev = new boolean[m];
        int[] suffix = new int[m];
        generateAllChildStrRelation(template, m, prev, suffix);
        //2.遍历主串从0-> n-m+1
        while (beginIndex <= n - m) {
            int j;
            for (j = m - 1; j >= 0; j--) {
                if (main[j + beginIndex] != template[j]) break;
            }
            //3.查看是否匹配，若匹配直接返回当前主串头索引
            if (j == -1) return beginIndex;
            //4.若不匹配,获取最后一个坏字符Si，并且生成散列表
            //5.散列表中查找坏字符，若不存在，则Xi = -1 ， 若存在则Xi = 散列表的value
            int xi = hashTable[main[j + beginIndex]];
            //6.计算坏字符的移动步数
            int bStep = j - xi;
            //8.好字符移动步数计算。
            int gStep = 0;
            if (j < m - 1) gStep = calculateGoodStep(prev, suffix, j, m);
            //9.比较步数返回较大值即可
            beginIndex += Math.max(bStep, gStep);
        }
        return -1;
    }

    /**
     * 计算好后缀的移动步长
     *
     * @param prev   查看某长度的模式串是否是前缀子串
     * @param suffix 查看某长度的模式串的在模式串中的起始index，不存在为-1
     * @param j      坏字符的index
     * @param m      模板串长度
     * @return 好后缀移动步长。
     */
    private int calculateGoodStep(boolean[] prev, int[] suffix, int j, int m) {
        //迭代好后缀字符串长度
        for (int i = j + 1; i <= m - 1; i++) {
            //若模板串中存在对应的前缀子串，则返回移动步数
            if (prev[m - i]) return j + 1 - suffix[i];
        }
        return m;
    }

    public static void main(String[] args) {
        String main = "abcabcdcabbbca";
        String template1 = "cdca";
        BMAlgorithm bm = new BMAlgorithm();
        System.out.println(bm.bm(main, template1));
    }
}
