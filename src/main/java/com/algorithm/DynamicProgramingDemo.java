package com.algorithm;

/**
 * 动态规划Demo
 */
public class DynamicProgramingDemo {

    public static void main(String[] args) {

//        System.out.println(dynamicAction1(new int[]{2, 2, 11, 43, 22}, 5, 14));
//        System.out.println(dynamicAction2(new int[]{2, 2, 11, 43, 22}, 5, 14));
//        dynamicAction4(new int[]{20, 40, 30, 10, 50}, 5, 83);
//        int[][] matrix = {{1, 3, 5, 9}, {2, 1, 3, 4}, {5, 2, 6, 7}, {6, 8, 4, 3}};
//        backTracingAction(0, 0, 0, matrix, 4);
//        System.out.println(minDist);
//        dynamicActionMatrix(matrix, 4);
//        System.out.println(dynamicActionMatrixWithStateEquation(3, 3));
//        int[][] matrix = {{5}, {7, 8}, {2, 3, 4}, {4, 9, 6, 1}, {2, 7, 9, 4, 5}};
//        System.out.println(yanghuiTriangle(matrix));
//        System.out.println(dynamicAction6(new int[]{1, 3, 5}, 9));
//        char[] a = "helloworld".toCharArray();
//        char[] b = "hellwoldad".toCharArray();
//        backTracingWithlwst(0, 0, 0);
//        System.out.println(MIN);
//        System.out.println(dynamicWithlwst(a, a.length, b, b.length));
//        System.out.println(maxCommonChildStrWithDynamic(a, a.length, b, b.length));
        System.out.println(maxSubStrWithDp(new int[]{2, 3, 6, 4, 6, 5}));
    }


    /**
     * action1: 背包问题。从n个不同重量的物体中选一部分填充最多能放置m的背包，让背包中质量最大
     * 1. 使用回溯，时间复杂度高。
     * 2. 使用动态规划，根据上一阶段的值来推断下一阶段。
     */
    public static int dynamicAction1(int[] weight, int n, int m) {
        boolean[][] states = new boolean[n][m + 1]; //记录层级状态和总重量的关系。
        //初始化第一个物品
        states[0][0] = true;
        if (weight[0] <= m) states[0][weight[0]] = true;
        //动态规划推断
        for (int i = 1; i < n; i++) {
            //物品i不加入背包中
            for (int j = 0; j <= m; j++) {
                //根据前面的状态来获取本层状态。
                if (states[i - 1][j]) states[i][j] = true;
            }
            //物品加入背包中,要确保总重量在m范围内
            for (int j = 0; j <= m - weight[i]; j++) {
                if (states[i - 1][j]) states[i][j + weight[i]] = true;
            }
        }
        //从最后一层获取最后一个为true的就是结果
        for (int i = m; i >= 0; i--) {
            if (states[n - 1][i])
                return i;
        }
        return -1;
    }

    /**
     * action1的空间复杂度优化版，将多层整合到一起。注意的是在动态规划状态转移的时候需要从大到小遍历。
     * 不然可能会重复遍历。
     */
    public static int dynamicAction2(int[] weight, int n, int m) {
        boolean[] state = new boolean[m + 1];
        //特殊处理第一个物品
        state[0] = true;
        if (weight[0] <= m) state[weight[0]] = true;
        //动态规划
        for (int i = 1; i < n; i++) {
            //这里必须要从大到小遍历，不然会导致重复遍历
            for (int j = m - weight[i]; j >= 0; j--) {
                if (state[j]) state[j + weight[i]] = true;
            }
        }
        for (int i = m; i >= 0; --i) {
            if (state[i]) return i;
        }
        return -1;
    }

    /**
     * 动态规划demo,背包问题扩展，在满足在m重量背包中获取满足重量的的最大价值
     * 只要让state状态数组保存的数据为当前层最大价值即可。
     *
     * @param weight 物品重量数组
     * @param price  物品价格数组
     * @param n      物品个数
     * @param m      背包容量
     */
    public static void dynamicAction3(int[] weight, int[] price, int n, int m) {
        int[][] states = new int[n][m + 1];
        //init states
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m + 1; j++) {
                states[i][j] = -1;
            }
        }
        //init first object
        states[0][0] = 0;
        if (price[0] >= 0) states[0][weight[0]] = price[0];
        //动态规划，根据上层逐渐计算下层状态
        for (int i = 1; i < n; i++) {
            //第i个物品不放入
            for (int j = 0; j <= m; j++) {
                if (states[i - 1][j] >= 0) states[i][j] = states[i - 1][j];
            }
            //第i个物品放入,j的取值保证总重量在m内
            for (int j = 0; j <= m - weight[i]; j++) {
                if (states[i - 1][j] >= 0) {
                    int max = states[i - 1][j] + price[i];
                    //取代那些较小的元素
                    if (max > states[i][j + weight[i]]) states[i][j + weight[i]] = max;
                }
            }
        }
        int max = 0;
        for (int i = 0; i < m + 1; i++) {
            max = Math.max(states[n - 1][i], max);
        }
        System.out.println(max);
    }

    /**
     * 有n件商品，从中选择一定商品，达到200元，如果让总金额最小。
     *
     * @param price 价格数组
     * @param n     物品数量
     * @param m     要达到价格
     */
    public static void dynamicAction4(int[] price, int n, int m) {
        //最多价格为2倍，不然就没必要去为了这个满减而去花更多的钱
        boolean[][] states = new boolean[n][2 * m + 1];
        //初始第一件物品
        states[0][0] = true;
        if (price[0] <= 2 * m) states[0][price[0]] = true;
        for (int i = 1; i < n; i++) {
            //不放入i物品
            for (int j = 0; j <= 2 * m; j++) {
                if (states[i - 1][j]) states[i][j] = true;
            }
            //放入物品i
            for (int j = 0; j <= 2 * m - price[i]; j++) {
                if (states[i - 1][j]) states[i][j + price[i]] = true;
            }
        }
        //获取最小值
        int min;
        for (min = m; min < 2 * m + 1; min++) {
            if (states[n - 1][min]) break;
        }
        System.out.println("min :" + min);
        //[m,2m]中没有商品符合直接退出
        if (min == 2 * m + 1) return;
        //从最后一层逐层向上获取添加进来的物品
        for (int i = n - 1; i >= 1; --i) {
            //1.min对应的上层不为true
            //2.对应的上层值-price[上层] 为true
            if (min - price[i] >= 0 && states[i - 1][min - price[i]]) {
                System.out.print(price[i] + " ");
                min = min - price[i];
            }
        }
        //说明第一件物品也参与了。
        if (min != 0) System.out.print(price[0]);
    }

    private static int minDist = Integer.MAX_VALUE;

    /**
     * 回溯解决矩阵最短路径
     *
     * @param row    当前行
     * @param column 当前列
     * @param matrix 二维矩阵
     * @param n      矩阵长
     */
    public static void backTracingAction(int row, int column, int dist, int[][] matrix, int n) {
        dist += matrix[row][column];
        if (row == n - 1 && column == n - 1) {
            if (dist < minDist) minDist = dist;
            return;
        }
        if (row < n - 1) backTracingAction(row + 1, column, dist, matrix, n);
        if (column < n - 1) backTracingAction(row, column + 1, dist, matrix, n);
    }

    /**
     * 动态规划解决矩阵最短路径
     *
     * @param matrix 矩阵
     * @param n      矩阵长宽
     */
    public static void dynamicActionMatrix(int[][] matrix, int n) {
        //创建状态数组
        int[][] states = new int[n][n];
        int sum = 0;
        //初始化第一行
        for (int i = 0; i < n; i++) {
            sum += matrix[0][i];
            states[0][i] = sum;
        }
        sum = 0;
        //初始化第一列
        for (int i = 0; i < n; i++) {
            sum += matrix[i][0];
            states[i][0] = sum;
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                states[i][j] = matrix[i][j] + Math.min(states[i - 1][j], states[i][j - 1]);
            }
        }
        System.out.println(states[n - 1][n - 1]);
    }

    //已经调用过的状态元素
    private static int[][] alreadyCall = new int[4][4];
    private static int[][] matrix = {{1, 3, 5, 9}, {2, 1, 3, 4}, {5, 2, 6, 7}, {6, 8, 4, 3}};

    /**
     * 状态方程求解矩阵最短路径问题
     *
     * @param row    行
     * @param column 列
     *               状态转移方程：min_dist(i,j) = matrix[i][j] + Math.min(min_dist(i-1,j) , min_dist(i,j-1))
     */
    public static int dynamicActionMatrixWithStateEquation(int row, int column) {
        if (row == 0 && column == 0) return matrix[0][0];
        if (alreadyCall[row][column] > 0) return alreadyCall[row][column];
        //依次递归获取两边最小值
        int minLeft = Integer.MAX_VALUE;
        if (column - 1 >= 0) minLeft = dynamicActionMatrixWithStateEquation(row, column - 1);
        int minUp = Integer.MAX_VALUE;
        if (row - 1 >= 0) minUp = dynamicActionMatrixWithStateEquation(row - 1, column);
        alreadyCall[row][column] = matrix[row][column] + Math.min(minLeft, minUp);
        return alreadyCall[row][column];
    }

    /**
     * 计算类'杨辉'三角的最短长度。画出状态表即可编码。
     * int[][] matrix = {{5}, {7, 8}, {2, 3, 4}, {4, 9, 6, 1}, {2, 7, 9, 4, 5}};
     * 5
     * 7 8
     * 2 3 4
     * 4 9 6 1
     * 2 7 9 4 5
     * 分三种情况，若为第一个元素则最短路径为上一行元素+自己
     * 若为最后一个元素则为state[i-1][j]+自己
     * 若为中间元素则为state[i-1][j-1]和state[i-1][j]中的较小值+自己
     */
    public static int yanghuiTriangle(int[][] matrix) {
        int[][] state = new int[matrix.length][matrix.length];
        state[0][0] = matrix[0][0];
        //构造动态规划状态表
        for (int i = 1; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (j == 0) state[i][j] = state[i - 1][j] + matrix[i][j];
                else if (j == matrix[i].length - 1) state[i][j] = state[i - 1][j - 1] + matrix[i][j];
                else state[i][j] = Math.min(state[i - 1][j - 1], state[i - 1][j]) + matrix[i][j];
            }
        }
        //求最小值
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < matrix[matrix.length - 1].length; i++) {
            minDist = Math.min(minDist, state[matrix.length - 1][i]);
        }
        return minDist;
    }

    /**
     * 硬币找零动态规划版本
     * 有三种不同硬币1，3，5元，支付9元的最少需要多少个硬币
     * f(9) = 1 + min(f(8),f(6),f(4))
     */
    public static int dynamicAction6(int[] price, int max) {
        int[][] states = new int[max][max + 1];
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < max + 1; j++) {
                states[i][j] = -1;
            }
        }
        //init first
        for (int i = 0; i < price.length; i++) {
            if (max >= price[i]) states[0][price[i]] = price[i];
        }
        int minNum = -1;
        OUTER:
        for (int i = 1; i < max; i++) {
            for (int j = 1; j < max + 1; j++) {
                if (states[i - 1][j] != -1) {
                    for (int k = 0; k < price.length; k++) {
                        if (j + price[k] <= max) states[i][j + price[k]] = price[k];
                    }
                }
                if (states[i][max] >= 0) {
                    minNum = i + 1;
                    break OUTER;
                }
            }
        }
        int tmp = max;
        for (int i = minNum - 1; i >= 0; i--) {
            for (int j = max; j >= 0; j--) {
                if (j == tmp) {
                    System.out.println(states[i][j]);
                    tmp = tmp - states[i][j];
                    break;
                }
            }
        }
        return minNum;
    }

    public static int min = Integer.MAX_VALUE;

    public static void backTracingWithAction6(int[] price, int remain, int minCount) {
        if (remain == 0) {
            if (minCount < min) min = minCount;
            return;
        }
        minCount++;
        if (remain - price[0] >= 0) backTracingWithAction6(price, remain - price[0], minCount);
        if (remain - price[1] >= 0) backTracingWithAction6(price, remain - price[1], minCount);
        if (remain - price[2] >= 0) backTracingWithAction6(price, remain - price[2], minCount);
    }

    /**
     * 使用回溯算法实现莱文斯坦距离(一对字符串的最短编辑距离，莱文斯坦可以通过增加，减少或者替换来完成字符串完全匹配，最小的编辑次数成为莱文斯坦距离)
     */
    private static char[] a = "mitcmu".toCharArray();
    private static char[] b = "mtacnu".toCharArray();
    private static int n = a.length;
    private static int m = b.length;
    private static int MIN = Integer.MAX_VALUE;

    /**
     * @param i        当前a字符串的索引i
     * @param j        当前b字符串的索引i
     * @param editStep 编辑步数
     */
    public static void backTracingWithlwst(int i, int j, int editStep) {
        //当某一字符串到达末尾
        if (i == n || j == m) {
            //完成后某一方没有到达末尾则需要补齐步数
            if (i < n) editStep += (n - i);
            if (j < m) editStep += (n - j);
            MIN = Math.min(MIN, editStep);
            return;
        }
        //递归回溯
        if (a[i] == b[j]) {
            backTracingWithlwst(i + 1, j + 1, editStep); //当两位置所在字符相等则都下一个字符继续递归
        } else {
            backTracingWithlwst(i, j + 1, editStep + 1); //i前面添加或者删除j继续比较
            backTracingWithlwst(i + 1, j, editStep + 1); //删除i或者添加j
            backTracingWithlwst(i + 1, j + 1, editStep + 1); //替换
        }
    }

    /**
     * 动态规划解决莱文斯坦问题
     * 状态转移方程为
     * if a[i]!=b[j]  那么 f(i,j) = min( f(i-1,j) + 1 , f(i , j-1) + 1 , f(i-1 , j-1 )+1 )
     * if a[i]==b[j]  那么 f(i,j) = min( f(i-1,j) + 1 , f(i , j-1) + 1 , f(i-1 , j-1 ) )
     */
    public static int dynamicWithlwst(char[] a, int i, char[] b, int j) {
        //状态转移方程
        int[][] states = new int[i][j];
        //初始化第一行
        for (int k = 0; k < i; k++) {
            if (b[0] == a[k]) states[0][k] = k; //当第k个字符相等的时候，那么此时已经走了k步
            else if (k != 0) states[0][k] = states[0][k - 1] + 1; //当字符不相等且k!=0的时候，则会等于前一个值得最小编辑次数+1
            else states[0][k] = 1; //当k为0且不相等时候为1
        }
        //初始化第一列
        for (int k = 0; k < j; k++) {
            if (a[0] == b[k]) states[k][0] = k;
            else if (k != 0) states[k][0] = states[k - 1][0] + 1;
            else states[k][0] = 1;
        }

        for (int k = 1; k < i; k++) {
            for (int l = 1; l < j; l++) {
                if (a[k] == b[l]) {
                    states[k][l] = Math.min(Math.min(states[k - 1][l - 1], states[k - 1][l] + 1), states[k][l - 1] + 1);
                } else {
                    states[k][l] = Math.min(Math.min(states[k - 1][l - 1] + 1, states[k - 1][l] + 1), states[k][l - 1] + 1);
                }
            }
        }
        return states[i - 1][j - 1];
    }

    /**
     * 动态规划解决最长公共子串长度(在比较的时候只允许增加或者删除元素)
     * 状态转移方程为
     * if a[i]!=b[j]  那么 f(i,j) = min( f(i-1,j) + 1 , f(i , j-1) + 1)
     * if a[i]==b[j]  那么 f(i,j) = min( f(i-1,j) + 1 , f(i , j-1) + 1 , f(i-1 , j-1 ) )
     */
    public static int maxCommonChildStrWithDynamic(char[] a, int m, char[] b, int n) {
        //状态表
        int[][] states = new int[m][n];
        //init row of zero
        for (int i = 0; i < n; i++) {
            if (a[0] == b[i]) states[i][0] = 1;
            else if (i != 0) states[i][0] = states[i - 1][0];
            else states[i][0] = 0;
        }
        for (int i = 0; i < m; i++) {
            if (b[0] == a[i]) states[0][i] = 1;
            else if (i != 0) states[0][i] = states[0][i - 1];
            else states[0][i] = 0;
        }

        //动态转移
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (a[i] == b[j]) {
                    states[i][j] = Math.max(Math.max(states[i - 1][j - 1] + 1, states[i - 1][j]), states[i][j - 1]);
                } else {
                    states[i][j] = Math.max(states[i - 1][j], states[i][j - 1]);
                }
            }
        }
        return states[m - 1][n - 1];
    }

    /**
     * 求一组数字中最大自增子串的长度
     * f(n) = 1 + f(n-1) 指的是前面所有的子串(关键在于如果求取前面所有字符串的最长子串长度。这里需要假设所有子串的长度已经求出。)中最长+1
     */
    public static int maxSubStrWithDp(int[] array) {
        if (array.length < 2) return array.length;
        //状态表
        int[] state = new int[array.length];
        //初始状态表
        state[0] = 1;

        for (int i = 1; i < array.length; i++) {
            //获取到子串中最长的
            int subMax = 0;
            for (int j = 0; j < i; j++) {
                if (array[j] < array[i]) {
                    subMax = Math.max(subMax, state[j]);
                }
            }
            state[i] = subMax + 1;
        }

        //获取array中最大值即可
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            result = Math.max(result, state[i]);
        }
        return result;
    }
}
