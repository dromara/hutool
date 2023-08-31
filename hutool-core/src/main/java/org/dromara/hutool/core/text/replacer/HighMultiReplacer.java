/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.text.replacer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 高效替换器，通过查找指定关键字，替换对应的值
 * 基于AC自动机算法实现，需要被替换的原字符串越大，替换的键值对越多，效率提升越明显
 * <p>
 * 注意: 如果需要被替换的关键字出现交叉,最先匹配中的关键字会被替换
 * 1、"abc","ab"   会优先替换"ab"
 * 2、"abed","be"  会优先替换"abed"
 * 3、"abc", "bc"  会优先替换"abc"
 *
 * @author kyao
 */
public class HighMultiReplacer extends StrReplacer {
	private static final long serialVersionUID = 1L;

	private final AhoCorasickAutomaton ahoCorasickAutomaton;

    /**
     * 构造
     *
     * @param map key为需要被查找的字符串，value为对应的替换的值
     */
    public HighMultiReplacer(final Map<String, String> map) {
        ahoCorasickAutomaton = new AhoCorasickAutomaton(map);
    }

    /**
     * 生成一个HighMultiReplacer对象
     *
     * @param map key为需要被查找的字符串，value为对应的替换的值
     * @return org.dromara.hutool.core.text.replacer.HighMultiReplacer
     */
    public static HighMultiReplacer of(final Map<String, String> map) {
        return new HighMultiReplacer(map);
    }

    @Override
    protected int replace(final CharSequence str, final int pos, final StringBuilder out) {
        ahoCorasickAutomaton.replace(str, out);
        return str.length();
    }

    @Override
    public CharSequence apply(final CharSequence str) {
        final StringBuilder builder = new StringBuilder();
        replace(str, 0, builder);
        return builder;
    }

    /**
     * AC自动机
     */
    private static class AhoCorasickAutomaton {

        /*AC自动机的根结点，根结点不存储任何字符信息*/
        private final Node root;

        /*待查找的目标字符串集合*/
        private final Map<String, String> target;

        /**
         * @param target 待查找的目标字符串集合
         */
        public AhoCorasickAutomaton(final Map<String, String> target) {
            root = new Node();
            this.target = target;
            buildTrieTree();
            buildAcFromTrie();
        }

        /**
         * 用于表示AC自动机的每个结点，在每个结点中我们并没有存储该结点对应的字符
         */
        private static class Node {

            /*如果该结点是一个终点，即，从根结点到此结点表示了一个目标字符串，则str != null, 且str就表示该字符串*/
            String str;

            /*该节点下的子节点*/
            Map<Character, Node> children = new HashMap<>();

            /*当前结点的孩子结点不能匹配文本串中的某个字符时，下一个应该查找的结点*/
            Node fail;

            public boolean isWord() {
                return str != null;
            }

        }

        /**
         * 由目标字符串构建Trie树
         */
        private void buildTrieTree() {
            for (final String targetStr : target.keySet()) {
                Node curr = root;
                if (targetStr == null) {
                    continue;
                }
                for (int i = 0; i < targetStr.length(); i++) {
                    final char ch = targetStr.charAt(i);
                    Node node = curr.children.get(ch);
                    if (node == null) {
                        node = new Node();
                        curr.children.put(ch, node);
                    }
                    curr = node;
                }
                /*将每个目标字符串的最后一个字符对应的结点变成终点*/
                curr.str = targetStr;
            }
        }

        /**
         * 由Trie树构建AC自动机，本质是一个自动机，相当于构建KMP算法的next数组
         */
        private void buildAcFromTrie() {
            /*广度优先遍历所使用的队列*/
            final LinkedList<Node> queue = new LinkedList<>();

            /*单独处理根结点的所有孩子结点*/
            for (final Node x : root.children.values()) {
                /*根结点的所有孩子结点的fail都指向根结点*/
                x.fail = root;
                queue.addLast(x);/*所有根结点的孩子结点入列*/
            }

            while (!queue.isEmpty()) {
                /*确定出列结点的所有孩子结点的fail的指向*/
                final Node p = queue.removeFirst();
                for (final Map.Entry<Character, Node> entry : p.children.entrySet()) {

                    /*孩子结点入列*/
                    queue.addLast(entry.getValue());
                    /*从p.fail开始找起*/
                    Node failTo = p.fail;
                    while (true) {
                        /*说明找到了根结点还没有找到*/
                        if (failTo == null) {
                            entry.getValue().fail = root;
                            break;
                        }

                        /*说明有公共前缀*/
                        if (failTo.children.get(entry.getKey()) != null) {
                            entry.getValue().fail = failTo.children.get(entry.getKey());
                            break;
                        } else {/*继续向上寻找*/
                            failTo = failTo.fail;
                        }
                    }

                }
            }
        }

        /**
         * 在文本串中替换所有的目标字符串
         *
         * @param text          被替换的目标字符串
         * @param stringBuilder 替换后的结果
         */
        public void replace(final CharSequence text, final StringBuilder stringBuilder) {
            Node curr = root;
            int i = 0;
            while (i < text.length()) {
                /*文本串中的字符*/
                final char ch = text.charAt(i);
                /*文本串中的字符和AC自动机中的字符进行比较*/
                final Node node = curr.children.get(ch);
                if (node != null) {
                    stringBuilder.append(ch);
                    /*若相等，自动机进入下一状态*/
                    curr = node;
                    if (curr.isWord()) {
                        stringBuilder.delete(stringBuilder.length() - curr.str.length(), stringBuilder.length());
                        stringBuilder.append(target.get(curr.str));
                        curr = root;
                    }
                    /*索引自增，指向下一个文本串中的字符*/
                    i++;
                } else {
                    /*若不等，找到下一个应该比较的状态*/
                    curr = curr.fail;
                    /*到根结点还未找到，说明文本串中以ch作为结束的字符片段不是任何目标字符串的前缀，状态机重置，比较下一个字符*/
                    if (curr == null) {
                        stringBuilder.append(ch);
                        curr = root;
                        i++;
                    }
                }
            }
        }

    }
}
