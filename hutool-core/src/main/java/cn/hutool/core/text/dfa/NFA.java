package cn.hutool.core.text.dfa;

import java.util.*;

/**
 * <p>
 *
 * 基于非确定性有穷自动机（NFA） 实现的多模匹配工具
 *
 * @author renyp
 */
public class NFA {
	private final Node root;

	/**
	 * 默认构造
	 */
	public NFA() {
		this.root = new Node();
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
		Node p = root;
		for (final char curr : word.toCharArray()) {
			if (p.next.get((int) curr) == null) {
				p.next.put((int) curr, new Node());
			}
			p = p.next.get((int) curr);
		}
		p.flag = true;
		p.str = word;
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
	public void buildAc() {
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
	}

	/**
	 * @param text 查询的文本（母串）
	 * @return 关键字列表
	 */
	public List<FoundWord> find(final String text) {
		return this.find(text, true);
	}

	/**
	 * @param text           查找的文本（母串）
	 * @param isDensityMatch 是否密集匹配
	 * @return 关键字列表
	 */
	public List<FoundWord> find(final String text, final boolean isDensityMatch) {
		final List<FoundWord> ans = new ArrayList<>();
		Node p = root, k;
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

		boolean flag;
		Node fail;
		String str;
		Map<Integer, Node> next;

		public Node() {
			this.flag = false;
			next = new HashMap<>();
		}
	}
}
