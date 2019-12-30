package com.algorithm;

import com.sun.xml.internal.fastinfoset.tools.XML_SAX_StAX_FI;

/**
 * 回溯算法demo
 * 回溯算法其实就是暴力枚举法，递归出所有路线，然后通过剪枝的方式来在找到结果的时候进行退出递归处理。
 * 回溯算法思想很适合用递归来进行解决,为什么？因为多个或者多层方向需要逐层去判断，若失败则回溯然后下一个方向继续。
 */
public class BackTracingAlgorithmDemo {
    public static void main(String[] args) {
        BackTracingAlgorithmDemo demo = new BackTracingAlgorithmDemo();
//        测试八皇后
//        demo.call8Queens(0);
//        测试0-1背包问题
        demo.package0Or1Question(0, 0, 0, new Package[]{
                new Package(10, 30),
                new Package(30, 20),
                new Package(20, 40),
                new Package(40, 70),
                new Package(50, 20),
                new Package(60, 40),
                new Package(100, 100)
        }, 5, 79);
        System.out.println(demo.max);
        System.out.println(demo.priceMax);

//        Pattern pattern = new Pattern("ab*");
//        System.out.println(pattern.match("abcde"));
    }

    private final int[] data = new int[8];
    private int times;

    /**
     * 八皇后问题
     */
    public void call8Queens(int row) {
        if (row >= 8) {
            //打印整个棋盘并且次数+1
            printQueens();
            times++;
            return;
        }

        //从当前行第一列开始计算，
        for (int column = 0; column < 8; column++) {
            //如果符合则给data赋值，data (index-> row value-> column)
            //不符合则回溯获取下一列。
            if (isOk(row, column)) {
                data[row] = column;
                //开始下一行的计算
                call8Queens(row + 1);
            }
        }
    }

    /**
     * 该位置是否满足八皇后规则
     *
     * @param row    行
     * @param column 列
     * @return true/false
     */
    private boolean isOk(int row, int column) {
        int left = column - 1, right = column + 1;
        //逐行向上比较
        for (int curRow = row - 1; curRow >= 0; curRow--) {
            if (data[curRow] == column) return false;
            if (left >= 0 && data[curRow] == left) return false;
            if (right < 8 && data[curRow] == right) return false;
            left--;
            right++;
        }
        return true;
    }

    private void printQueens() {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (data[row] == column) {
                    System.out.print("Q ");
                }
                System.out.print("* ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println(times);
    }

    private int max = Integer.MIN_VALUE;
    private int priceMax = Integer.MIN_VALUE;

    /**
     * 0-1背包问题（回溯算法解决）
     * m重量的背包，n个物品，获取背包能装下的最大重量。
     * 其实这个问题抽象成回溯解决就编程n次放置，分别有两种方案，放，不放
     * i -> 当前物品
     * sum -> 之前所有物品质量总和
     * items->所有物品集合
     * n -> 物品个数
     * m -> 物品总值
     */
    public void package0Or1Question(int i, int sum, int weightSum, Package[] items, int n, int m) {
        //当已经无法防止，则停止递归(剪枝)
        if (sum >= m || i == n) {
            max = Math.max(sum, max);
            priceMax = Math.max(weightSum, priceMax);
            return;
        }
        //不放
        package0Or1Question(i + 1, sum, weightSum, items, n, m);
        //放，满足前提的情况下。
        if (sum + items[i].getWeight() <= m) {
            package0Or1Question(i + 1, sum + items[i].getWeight(), weightSum + items[i].getPrice(), items, n, m);
        }
    }

    static class Package {
        private int price;
        private int weight;

        public Package(int price, int weight) {
            this.price = price;
            this.weight = weight;
        }

        public int getPrice() {
            return price;
        }

        public int getWeight() {
            return weight;
        }
    }

    /**
     * 自定义正则表达
     */
    static class Pattern {
        private boolean matched = false;
        private char[] pattern;
        private int pl;

        public Pattern(String regex) {
            this.pattern = regex.toCharArray();
            this.pl = this.pattern.length;
        }

        public boolean match(String text) {
            doMatch(0, 0, text.toCharArray(), text.length());
            return matched;
        }

        private void doMatch(int pi, int ti, char[] text, int tl) {
            //如果已经匹配那么return
            if (matched) return;
            //如果表达式和待匹配文本都到达结尾那么return
            if (pi == pl) {
                if (ti == tl) matched = true;
                return;
            }
            //如果为*，则可以匹配任意字符
            if (pattern[pi] == '*') {
                for (int i = 0; i <= tl - ti; i++) {
                    doMatch(pi + 1, ti + i, text, tl);
                }
            } else if (pattern[pi] == '?') {
                //匹配0个或者1个
                doMatch(pi + 1, ti, text, tl);
                doMatch(pi + 1, ti + 1, text, tl);
            } else if (ti < tl && pattern[pi] == text[ti]) {
                doMatch(pi + 1, ti + 1, text, tl);
            }
        }
    }
}
