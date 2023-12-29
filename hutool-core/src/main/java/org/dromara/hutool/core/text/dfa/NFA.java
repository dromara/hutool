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

package org.dromara.hutool.core.text.dfa;

import java.util.*;

/**
 * <p>
 * 基于非确定性有穷自动机（NFA） 实现的多模匹配工具
 * </p>
 *
 * @author renyp
 */
public class NFA {
	/**
	 * AC树的根节点
	 */
	private final Node root;
	/**
	 * 标记是否需要构建AC自动机，做树优化
	 */
	private volatile boolean needBuildAc;

	/**
	 * 内置锁，防止并发场景，并行建AC树，造成不可预知结果
	 */
	private final Object buildAcLock;

	/**
	 * 内置锁，防止并行插入，新节点建立后，被挂载到树上前 被篡改
	 */
	private final Object insertTreeLock;

	/**
	 * 默认构造
	 */
	public NFA() {
		this.root = new Node();
		this.needBuildAc = true;
		this.buildAcLock = new Object();
		this.insertTreeLock = new Object();
	}

	/**
	 * 构造函数 并 初始化词库
	 *
	 * @param words 添加的新词
	 */
	public NFA(final String... words) {
		this();
		this.insert(words);
	}

	/**
	 * 词库添加新词，初始化查找树
	 *
	 * @param word 添加的新词
	 */
	public void insert(final String word) {
		synchronized (insertTreeLock) {
			needBuildAc = true;
			Node p = root;
			for (final char curr : word.toCharArray()) {
				p.next.computeIfAbsent((int) curr, k -> new Node());
				p = p.next.get((int) curr);
			}
			p.flag = true;
			p.str = word;
		}
	}

	/**
	 * 词库批量添加新词，初始化查找树
	 *
	 * @param words 添加的新词
	 */
	public void insert(final String... words) {
		for (final String word : words) {
			this.insert(word);
		}
	}

	/**
	 * 构建基于NFA模型的 AC自动机
	 */
	private void buildAc() {
		final Queue<Node> queue = new LinkedList<>();
		final Node p = root;
		for (final Integer key : p.next.keySet()) {
			p.next.get(key).fail = root;
			queue.offer(p.next.get(key));
		}
		while (!queue.isEmpty()) {
			final Node curr = queue.poll();
			for (final Integer key : curr.next.keySet()) {
				Node fail = curr.fail;
				// 查找当前节点匹配失败，他对应等效匹配的节点是哪个
				while (fail != null && fail.next.get(key) == null) {
					fail = fail.fail;
				}
				// 代码到这，有两种可能，fail不为null，说明找到了fail；fail为null，没有找到，那么就把fail指向root节点（当到该节点匹配失败，那么从root节点开始重新匹配）
				if (fail != null) {
					fail = fail.next.get(key);
				} else {
					fail = root;
				}
				curr.next.get(key).fail = fail;
				queue.offer(curr.next.get(key));
			}
		}
		needBuildAc = false;
	}

	/**
	 * @param text 查询的文本（母串）
	 * @return FoundWord列表，查找到的所有关键词
	 */
	public List<FoundWord> find(final String text) {
		return this.find(text, true);
	}

	/**
	 * @param text           查找的文本（母串）
	 * @param isDensityMatch 是否密集匹配
	 * @return FoundWord列表，查找到的所有关键词
	 */
	public List<FoundWord> find(final String text, final boolean isDensityMatch) {
		// double check，防止重复无用的 buildAC
		if (needBuildAc) {
			synchronized (buildAcLock) {
				if (needBuildAc) {
					this.buildAc();
				}
			}
		}
		final List<FoundWord> ans = new ArrayList<>();
		Node p = root, k = null;
		for (int i = 0, len = text.length(); i < len; i++) {
			final int ind = text.charAt(i);
			// 状态转移(沿着fail指针链接的链表，此处区别于DFA模型)
			while (p != null && p.next.get(ind) == null) {
				p = p.fail;
			}
			if (p == null) {
				p = root;
			} else {
				p = p.next.get(ind);
			}
			// 提取结果(沿着fail指针链接的链表，此处区别于DFA模型)
			k = p;
			while (k != null) {
				if (k.flag) {
					ans.add(new FoundWord(k.str, k.str, i - k.str.length() + 1, i));
					if (!isDensityMatch) {
						p = root;
						break;
					}
				}
				k = k.fail;
			}
		}
		return ans;
	}


	private static class Node {

		/**
		 * 当前节点是否是一个单词的结尾
		 */
		boolean flag;
		/**
		 * 指向 当前节点匹配失败应该跳转的下个节点
		 */
		Node fail;
		/**
		 * 以当前节点结尾的单词
		 */
		String str;
		/**
		 * 当前节点的子节点
		 */
		Map<Integer, Node> next;

		public Node() {
			this.flag = false;
			next = new HashMap<>();
		}
	}
}
