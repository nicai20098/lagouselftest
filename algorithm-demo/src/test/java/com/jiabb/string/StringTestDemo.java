package com.jiabb.string;

import org.junit.Test;

/**
 * @description: 字符串测试类
 * @author: jiabb-b
 * @date: 2024/1/31 031 15:14
 * @since: 1.0
 */
public class StringTestDemo {

    /**
     * KMP 种高效的字符串匹配算法
     * 核心思想是当模式串与文本串不匹配时，利用已经部分匹配的信息，尽量减少模式串与主串的匹配次数以达到快速匹配的目的。
     * KMP算法的关键组成部分包括：
     *      部分匹配表（Partial Match Table）：也称为next数组，它记录了模式串中每个位置之前的子串的最长相等前后缀的长度。这个表是在算法执行前预处理得到的，用于在发生不匹配时决定模式串的下一个比较位置。
     *      算法运行过程：KMP算法在匹配过程中，如果发现不匹配，会根据部分匹配表移动模式串，而不是文本串。这样可以避免不必要的字符比较，提高匹配效率。
     *      时间复杂度：KMP算法的时间复杂度为O(n+m)，其中n是文本串的长度，m是模式串的长度。相比于其他字符串匹配算法，如朴素的暴力匹配算法，KMP算法在最坏情况下仍然保持较高的效率。
     */
    @Test
    public void kmpTest() {
        String text = "ababdabacdababcabab";
        String pattern = "ababcabab";
//        String pattern = "abc";
        int index = kmpSearch(text, pattern);
        System.out.println("Pattern found at index: " + index);
    }

    /**
     * [-1, 0, 0, 1, 2, 0, 1, 2, 3]
     *   a  b  a  b  c  a  b  a  b
     *   a  b  a  b  d  a  b  a  c  d  a  b  a  b  c  a  b  a  b
     *   i = 4 j = 2
     *   a  b  a  b  d  a  b  a  c  d  a  b  a  b  c  a  b  a  b
     *   a  b  a  b  c  a  b  a  b
     *   i = 4 j = -1
     *
     *
     */
    public static int kmpSearch(String text, String pattern) {
        int[] next = getNext(pattern);
        int i = 0;
        int j = 0;
        while (i < text.length() && j < pattern.length()) {
            if (j == -1 || text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j == pattern.length()) {
            return i - j;
        } else {
            return -1;
        }
    }

    /**
     *          a  b  a  b  c  a  b  a  b
     * 初始化  [-1, 0, 0, 0, 0, 0, 0, 0, 0]
     *  k = -1  j = 0
     * 第一轮  0 < 9 - 1
     *  k = 0, j = 1
     *  [-1, 0, 0, 0, 0, 0, 0, 0, 0]
     * 第二次 1 < 9 - 1
     *  chat(1) = b chat(0) = a
     *  走 else  k = -1
     *  此时 j = 1, k = -1
     * 第三轮 1 < 9 - 1
     *  k = -1
     *  k = 0, j = 2
     *   [-1, 0, 0, 0, 0, 0, 0, 0, 0]
     * 第四轮 2 < 9 - 1
     * chat(2) = a  chat(0) = a
     * k = 1, j = 3
     *  [-1, 0, 0, 1, 0, 0, 0, 0, 0]
     * 第五轮 3 < 9 - 1
     * chat(3) = b chat(1) = b
     * k = 2, j = 4
     *  [-1, 0, 0, 1, 2, 0, 0, 0, 0]
     * 第六轮 4 < 9 - 1
     * chat(4) = c chat(2) = a
     * k = -1
     * 第七轮  4 < 9 - 1
     * k = 0, j = 5
     *  [-1, 0, 0, 1, 2, 0, 0, 0, 0]
     * 第八轮 5 < 9 - 1
     * chat(5) = a chat(0) = a
     * k = 1, j = 6
     *  [-1, 0, 0, 1, 2, 0, 1, 0, 0]
     *  第九轮 6 < 9 - 1
     *  chat(6) = b chat(1) = b
     *  k = 2, j = 7
     *  [-1, 0, 0, 1, 2, 0, 1, 2, 0]
     *  第十轮 7 < 9 - 1
     *  chat(7) = a chat(2) = a
     *  k = 3, j = 8
     *   [-1, 0, 0, 1, 2, 0, 1, 2, 3]
     */
    public static int[] getNext(String pattern) {
        int[] next = new int[pattern.length()];
        next[0] = -1;
        int k = -1;
        int j = 0;
        while (j < pattern.length() - 1) {
            if (k == -1 || pattern.charAt(j) == pattern.charAt(k)) {
                ++k;
                ++j;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }


}
