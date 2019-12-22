### 字符串匹配算法
- BF(Brute Force) ： 普通的暴力匹配法
- RK ： 使用Hash来将主串对应模式串的所有子串先使用特定hash算法获取hash值，然后再比较
- BM (Boyer-Moore) ：使用坏字符和好后缀两种模式来匹配字符串，性能很高
- KMP : 使用好前缀匹配来尽可能的跳跃重复比较。使用最为广泛

#### BM算法的实现

##### 坏字符
> 当遇到模式串和主串不匹配的时候，则从后向前获取到第一个不匹配的字符
```
主串 : a、b、c、d
模式串:a、c、c
此时坏字符就是b
```
当遇到坏字符的时候
- 记录怀字符串在模式串中的索引位置`a`
- 在模式串中寻找该字符的最大索引位置`b`(这里需要维护一个数组，索引为字符ASCII值，value为在模式串中的索引位置)
```java
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
```
- 移动的步数为 `a-b`
- 坏字符可能会不移动和逆向移动
```
主串:  b a a c
模式串：a b a
此时根据坏字符会逆向移动，则此时需要使用好后缀
```


##### 好后缀
> 解决坏字符不能使用的场景，将主串和模式串匹配的后缀继续在模式串中进行查询，若有则进行移动

- 记录坏字符串在模式串中的索引位置`j`
- 在维护好的数组中进行查找(这里的数组有两个，一个是suffix数组 index->子串长度 value->子串初始索引位置,另一个是prev布尔类型数组
index -> 子串长度,value->是否是模式串的前缀子串)，查找到则步长为`j+1-suffix(k)`,k为前缀长度，查找不到则步长为模式串的长度

```
    /**
     * 生成模式串的所有子串的关系
     * 这里利用了巧妙的迭代方法，变量i初始取值为[0,m-2]，依次和模式串进行公共后缀匹配，若相同则给
     * suffix[k]（k为长度）赋当前索引位置，
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
```

[完整实例代码](../src/main/java/com/algorithm/BMAlgorithm.java)

#### `KMP`算法实现

> 使用好前缀匹配并且使用next数组来提前预处理所有的好前缀子串

步骤：

- 预处理模式串的所有前缀子串和其最长后缀子串的关系数组`next`

```java
    /**
     * 构建next数组
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
          	//这里是因为 i 除了最后一个字符外，会被 i-1的最长后缀子串所包含。
            while(k!=-1 && template.charAt(i)!=template.charAt(k+1)) k = next[k];
            //当i和k+1的模式串字符相同的时候说明其为最长后缀子串
            if (template.charAt(i) == template.charAt(k+1)) k++;
            next[i] = k;
        }
        return next;
    }
```

- 迭代遍历 i ∈ [0,n-1]，并用`j`来记录坏字符在模式串中的索引位置
- 若`template[j] != main[i]`从预处理的`next`数组里面根据前缀长度(`j-1`)获取最长后缀的索引位置(`next[j-1]`)。将`j = next[j-1]`
- 若`template[j] == main[i]`，则`j++`, 

- 若`j==m`则说明完全匹配，返回` i-m +1`

```java
    /**
     * kmp算法
     *
     * @param main     主串
     * @param template 模式串
     */
    public static int kmp2(String main, String template) {
        int n = main.length();
        int m = template.length();
        //1.构建预处理next数组(index->前缀索引下标，v->该前缀的最长后缀子串的结尾索引)
        int[] next = getNext2(template);
        //2.定义坏字符位置
        int j = 0;
        //3.迭代i∈[0,n-1]
        for (int i = 0; i < n - 1; i++) {
          	//不匹配时，获取当前好前缀的最长后缀位置+1赋给新的坏字符位置j
            while (j > 0 && template.charAt(j) != main.charAt(i)) j = next[j - 1] + 1;
          //匹配则坏字符位置+1
            if (main.charAt(i) == template.charAt(j)) ++j;
          //若完全匹配则直接返回
            if (j == m) return i - m + 1;
        }
        return -1;
    }
```
[KMP实现实例](../src/main/java/com/algorithm/KMPStringMatchAlgorithm.java)
