package com.datastructure;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * 无向图Demo
 * 1. 广度优先搜索是能获取到最短路径的，需要借用visited来记录访问过的顶点，queue来记录为被访问过的子顶点，prev来记录路径过程
 * 2. 深度优先搜索不能获取到最短路径，需要借用visited，prev,且需要一个退出状态found。在找到目标t的时候退出递归。(回溯思想)
 * E为边的个数，V为顶点个数。
 * 时间复杂度O(E),空间复杂度O(V)
 */
public class GraphDemo {
    /* 顶点个数*/
    private int v;
    /*邻接表*/
    private LinkedList<Integer>[] adj;

    // default init graph
    public GraphDemo(int v) {
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    //添加边，无向图实现
    public void addEdge(int s, int t) {
        if (s < 0 || s >= v || t < 0 || t >= v) throw new IllegalArgumentException("Valid Index");
        adj[s].add(t);
        adj[t].add(s);
    }

    //广度优先搜索(breadth-first-search)
    //层级搜索，原理是通过
    // Boolean[] visited : 记录已经访问过的顶点
    // Queue<Integer> queue ： 记录访问过的顶点但相邻顶点未被访问
    // int[] prev ： 记录路径 prev[t] = s -> s是t的上一层相邻顶点
    // 实现的。将初始顶点入queue，并记录访问状态为true，然后循环
    // 1.获取邻接表元素
    // 2.如果没有被访问且不等于终点t的话讲该相邻元素入队列，修改访问状态，并记录访问路径
    // 3. 如果等于终点t.那么递归打印路径即可。
    public void bfs(int s, int t) {
        if (s < 0 || s >= v || t < 0 || t >= v) throw new IllegalArgumentException("Valid Index");
        if (s == t) return;
        //init visited arr and queue
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[v];
        int[] depth = new int[v];
        int[] prev = new int[v];
        for (int i = 0; i < v; i++) {
            prev[i] = -1;
            depth[i] = -1;
        }
        visited[s] = true;
        queue.add(s);
        depth[s] = 0;
        //当队列中元素不为空时
        while (!queue.isEmpty()) {
            //获取队列顶点
            final Integer cur = queue.poll();
            //遍历其邻接表
            for (int i = 0; i < adj[cur].size(); i++) {
                final Integer next = adj[cur].get(i);
                //如果next没有被访问过
                if (!visited[next]) {
                    //添加路径绑定信息
                    prev[next] = cur;
                    depth[next] = depth[cur] + 1;
                    if (next == t) {
                        //打印路径信息。
                        print(prev, t, s);
                        printDepth(depth);
                        return;
                    }
                    visited[next] = true;
                    queue.add(next);
                }
            }
        }
    }

    private void printDepth(int[] depth) {
        Map<Integer, List<Integer>> depthMap = Maps.newHashMap();
        for (int i = 0; i < v; i++) {
            final int d = depth[i];
            if (d != -1) {
                if (!depthMap.containsKey(d)) {
                    List<Integer> array = Lists.newArrayList();
                    array.add(i);
                    depthMap.put(d, array);
                } else {
                    depthMap.get(d).add(i);
                }
            }
        }
        depthMap.forEach((k, v) -> System.out.printf("第[%s]层,值%s\n", k, v.toString()));
    }

    //深度优先搜索->Depth-First-Search
    //回溯思想，如果当前顶点没有找到直接到子顶点找。直到找到
    public void dfs(int s, int t) {
        if (s < 0 || s >= v || t < 0 || t >= v) throw new IllegalArgumentException("Valid Index");
        boolean found = false;
        //初始化顶点状态arr和历史路径arr
        boolean[] visited = new boolean[v];
        int[] prev = new int[v];
        for (int i = 0; i < v; i++) {
            prev[i] = -1;
        }
        recursionDfs(s, t, visited, prev, found);
        print(prev, t, s);
    }

    //深度优先搜索递归
    private void recursionDfs(int s, int t, boolean[] visited, int[] prev, boolean found) {
        if (found) return;
        visited[s] = true;
        if (s == t) {
            found = true;
            return;
        }
        for (int i = 0; i < adj[s].size(); i++) {
            //获取子顶点
            Integer next = adj[s].get(i);
            if (!visited[next]) {
                prev[next] = s;
                recursionDfs(next, t, visited, prev, found);
            }
        }
    }

    private void foreach(Node[] prev, int depth) {
        Arrays.stream(prev)
                .filter(n -> n.depth == depth)
                .map(Node::toString)
                .forEach(System.out::println);
    }

    //递归打印路径
    //s-> 开始顶点
    //t-> 结束顶点
    private void print(int[] prev, int t, int s) {
        if (prev[t] != -1 && t != s) {
            print(prev, prev[t], s);
        }
        System.out.println(t + " ");
    }

    private void print(Node[] prev, int t, int s) {
        if (prev[t].value != -1 && t != s) {
            print(prev, prev[t].value, s);
        }
        System.out.println(t + " ");
    }


    public static void main(String[] args) {
        GraphDemo graph = new GraphDemo(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(3, 4);
        graph.addEdge(3, 2);
        graph.bfs(0, 4);
        System.out.println("-----");
        graph.dfs(1, 4);
    }

    static class Node {
        private int value;
        private int depth;

        public Node(int value, int depth) {
            this.value = value;
            this.depth = depth;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", depth=" + depth +
                    '}';
        }
    }
}
