package com.concurrent;

import java.util.*;

/**
 * @author hujing
 * @date Create in 2020/9/25
 * 图的相关算法实现
 **/
public class GraphDemo {

    public static void main(String[] args) {
        Graph graph = new Graph(6);
        graph.addEdge(4, 5);
        graph.addEdge(3, 4);
        graph.addEdge(3, 2);
        graph.addEdge(3, 1);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.bfs(0, 5, 2);
//        graph.dfs(5, 5);
    }


    static class Graph {
        /**
         * 顶点个数
         */
        private int v;

        /**
         * 对应的边
         */
        private LinkedList<Integer>[] edges;

        public Graph(int v) {
            this.v = v;
            edges = new LinkedList[v];
            for (int i = 0; i < v; i++) {
                edges[i] = new LinkedList<>();
            }
        }

        /**
         * 添加边
         *
         * @param s source
         * @param t target
         */
        public void addEdge(int s, int t) {
            if (s < 0 || s >= v || t < 0 || t >= v) {
                throw new IndexOutOfBoundsException();
            }
            edges[s].add(t);
            edges[t].add(s);

        }

        /**
         * 广度优先搜索算法
         */
        public void bfs(int s, int t, int n) {
            int[] result = new int[v];
            //ignore校验
            //辅助队列
            Queue<Integer> queue = new LinkedList<>();
            //记录是否访问
            boolean[] visited = new boolean[v];
            //保存prev,方便递归遍历图的轨迹
            int[] prev = new int[v];
            int[] depth = new int[v];
            Arrays.fill(prev, -1);
            Arrays.fill(depth, -1);
            queue.add(s);
            visited[s] = true;
            depth[s] = 0;
            while (!queue.isEmpty()) {
                Integer curV = queue.poll(); //当前顶点
                LinkedList<Integer> edges = this.edges[curV];
                for (int i = 0; i < edges.size(); i++) {
                    Integer edge = edges.get(i); //获取关联顶点
                    if (!visited[edge]) {
                        prev[edge] = curV;
                        depth[edge] = depth[curV] + 1;
                        if (edge == t) {
                            print(s, t, prev);
                            printDepth(n, depth);
                            return;
                        }
                        visited[edge] = true;
                        queue.add(edge);
                    }
                }
            }


        }

        private void printDepth(int n, int[] depth) {
            System.out.println("-----------");
            for (int i = 0; i < depth.length; i++) {
                if (depth[i]>0 && depth[i] <=n) {
                    System.out.println(i);
                }
            }
        }

        boolean founded = false;

        /**
         * 深度优先搜索算法
         *
         * @param s source
         * @param t target
         */
        public void dfs(int s, int t) {
            //ignore校验
            //记录是否访问
            boolean[] visited = new boolean[v];
            //保存prev,方便递归遍历图的轨迹
            int[] prev = new int[v];
            Arrays.fill(prev, -1);
            dfsRecursive(s, t, visited, prev);
            print(s, t, prev);
        }

        private void dfsRecursive(int s, int t, boolean[] visited, int[] prev) {
            if (founded) {
                return;
            }
            if (s == t) {
                founded = true;
                return;
            }
            visited[s] = true;
            LinkedList<Integer> edges = this.edges[s];
            for (int i = 0; i < edges.size(); i++) {
                Integer curV = edges.get(i);
                if (!visited[curV]) {
                    prev[curV] = s;
                    dfsRecursive(curV, t, visited, prev);
                }
            }
        }

        /**
         * 递归打印轨迹
         *
         * @param s
         * @param t
         * @param prev
         */
        private void print(int s, int t, int[] prev) {
            if (prev[t] != -1 && t != s) {
                print(s, prev[t], prev);
            }
            System.out.println(t);
        }
    }
}
