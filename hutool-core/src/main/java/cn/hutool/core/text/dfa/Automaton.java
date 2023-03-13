package cn.hutool.core.text.dfa;

import java.util.*;

/**
 * <p>
 *
 * 基于非确定性有穷自动机（NFA） 实现的多模匹配工具
 *
 * @author renyp
 */
public class Automaton {
	private final Node root;

	/**
	 * 默认构造
	 */
	public Automaton() {
		this.root = new Node();
	}

	/**
	 * 构造函数 并 初始化词库
	 *
	 * @param words 添加的新词
	 */
	public Automaton(String... words) {
		this();
		this.insert(words);
	}

	/**
	 * 词库添加新词，初始化查找树
	 *
	 * @param word 添加的新词
	 */
	public void insert(String word) {
		Node p = root;
		for (char curr : word.toCharArray()) {
			int ind = curr;
			if (p.next.get(ind) == null) {
				p.next.put(ind, new Node());
			}
			p = p.next.get(ind);
		}
		p.flag = true;
		p.str = word;
	}

	/**
	 * 词库批量添加新词，初始化查找树
	 *
	 * @param words 添加的新词
	 */
	public void insert(String... words) {
		for (String word : words) {
			this.insert(word);
		}
	}

	/**
	 * 构建基于NFA模型的 AC自动机
	 */
	public void buildAc() {
		Queue<Node> queue = new LinkedList<>();
		Node p = root;
		for (Integer key : p.next.keySet()) {
			p.next.get(key).fail = root;
			queue.offer(p.next.get(key));
		}
		while (!queue.isEmpty()) {
			Node curr = queue.poll();
			for (Integer key : curr.next.keySet()) {
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
	 */
	public List<FoundWord> find(String text) {
		return this.find(text, true);
	}

	/**
	 * @param text           查找的文本（母串）
	 * @param isDensityMatch 是否密集匹配
	 */
	public List<FoundWord> find(String text, boolean isDensityMatch) {
		List<FoundWord> ans = new ArrayList<>();
		Node p = root, k = null;
		for (int i = 0, len = text.length(); i < len; i++) {
			int ind = text.charAt(i);
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
