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

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.stream.EasyStream;
import org.dromara.hutool.core.text.StrUtil;

import java.util.*;
import java.util.function.Predicate;

/**
 * DFA（Deterministic Finite Automaton 确定有穷自动机）
 * DFA单词树（以下简称单词树），常用于在某大段文字中快速查找某几个关键词是否存在。<br>
 * 单词树使用group区分不同的关键字集合，不同的分组可以共享树枝，避免重复建树。<br>
 * 单词树使用树状结构表示一组单词。<br>
 * 例如：红领巾，红河 构建树后为：
 * <pre>
 *            红
 *            /\
 *          领  河
 *         /
 *       巾
 * </pre>
 * 其中每个节点都是一个WordTree对象，查找时从上向下查找。
 *
 * @author Looly
 */
public class WordTree extends HashMap<Character, WordTree> {
	private static final long serialVersionUID = -4646423269465809276L;

	/**
	 * 单词字符末尾标识，用于标识单词末尾字符
	 */
	private Set<Character> endCharacterSet = null;
	/**
	 * 字符过滤规则，通过定义字符串过滤规则，过滤不需要的字符，当accept为false时，此字符不参与匹配
	 */
	private Predicate<Character> charFilter = StopChar::isNotStopChar;

	//--------------------------------------------------------------------------------------- Constructor start

	/**
	 * 默认构造
	 */
	public WordTree() {
	}

	/**
	 * 指定初始化容量
	 *
	 * @param initialCapacity 初始容量，一般是关键词的数量
	 */
	public WordTree(final int initialCapacity) {
		super((int) (initialCapacity / MapUtil.DEFAULT_LOAD_FACTOR) + 1);
	}
	//--------------------------------------------------------------------------------------- Constructor start

	/**
	 * 设置字符过滤规则，通过定义字符串过滤规则，过滤不需要的字符<br>
	 * 当accept为false时，此字符不参与匹配
	 *
	 * @param charFilter 过滤函数
	 * @return this
	 * @since 5.2.0
	 */
	public WordTree setCharFilter(final Predicate<Character> charFilter) {
		this.charFilter = charFilter;
		return this;
	}

	//------------------------------------------------------------------------------- add word

	/**
	 * 增加一组单词
	 *
	 * @param words 单词集合
	 * @return this
	 */
	public WordTree addWords(Collection<String> words) {
		if (!(words instanceof Set)) {
			words = new HashSet<>(words);
		}
		for (final String word : words) {
			addWord(word);
		}
		return this;
	}

	/**
	 * 增加一组单词
	 *
	 * @param words 单词数组
	 *              @return this
	 */
	public WordTree addWords(final String... words) {
		for (final String word : SetUtil.of(words)) {
			addWord(word);
		}
		return this;
	}

	/**
	 * 添加单词，使用默认类型
	 *
	 * @param word 单词
	 * @return this
	 */
	public WordTree addWord(final String word) {
		if (null == word) {
			return this;
		}
		final Predicate<Character> charFilter = this.charFilter;
		WordTree parent = null;
		WordTree current = this;
		WordTree child;
		char currentChar = 0;
		final int length = word.length();
		for (int i = 0; i < length; i++) {
			currentChar = word.charAt(i);
			//只处理合法字符
			if (charFilter.test(currentChar)) {
				//无子节点，新建一个子节点后存放下一个字符，子节点的同级节点不会有太多同级节点，默认1个
				child = current.computeIfAbsent(currentChar, c -> new WordTree(1));
				parent = current;
				current = child;
			}
		}
		if (null != parent) {
			parent.setEnd(currentChar);
		}
		return this;
	}
	//------------------------------------------------------------------------------- match

	/**
	 * 指定文本是否包含树中的词
	 *
	 * @param text 被检查的文本
	 * @return 是否包含
	 */
	public boolean isMatch(final String text) {
		//被检查的文本大概率不是null，由里层方法统一校验即可
		return null != matchWord(text);
	}

	/**
	 * 获得第一个匹配的关键字
	 *
	 * @param text 被检查的文本
	 * @return 匹配到的关键字
	 */
	public String match(final String text) {
		final FoundWord foundWord = matchWord(text);
		return null != foundWord ? foundWord.toString() : null;
	}

	/**
	 * 获得第一个匹配的关键字
	 *
	 * @param text 被检查的文本
	 * @return 匹配到的关键字
	 * @since 5.5.3
	 */
	public FoundWord matchWord(final String text) {
		if (null == text) {
			return null;
		}
		final List<FoundWord> matchAll = matchAllWords(text, 1);
		return CollUtil.get(matchAll, 0);
	}

	//------------------------------------------------------------------------------- match all

	/**
	 * 找出所有匹配的关键字
	 *
	 * @param text 被检查的文本
	 * @return 匹配的词列表
	 */
	public List<String> matchAll(final String text) {
		return matchAll(text, -1);
	}

	/**
	 * 找出所有匹配的关键字
	 *
	 * @param text 被检查的文本
	 * @return 匹配的词列表
	 * @since 5.5.3
	 */
	public List<FoundWord> matchAllWords(final String text) {
		return matchAllWords(text, -1);
	}

	/**
	 * 找出所有匹配的关键字
	 *
	 * @param text  被检查的文本
	 * @param limit 限制匹配个数，如果小于等于0，则返回全部匹配结果
	 * @return 匹配的词列表
	 */
	public List<String> matchAll(final String text, final int limit) {
		return matchAll(text, limit, false, false);
	}

	/**
	 * 找出所有匹配的关键字
	 *
	 * @param text  被检查的文本
	 * @param limit 限制匹配个数，如果小于等于0，则返回全部匹配结果
	 * @return 匹配的词列表
	 * @since 5.5.3
	 */
	public List<FoundWord> matchAllWords(final String text, final int limit) {
		return matchAllWords(text, limit, false, false);
	}

	/**
	 * 找出所有匹配的关键字<br>
	 * <p>假如被检查文本是{@literal "abab"}<br>
	 * 密集匹配原则：假如关键词有 ab,b，将匹配 [ab,b,ab]<br>
	 * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[a, ab]
	 * </p>
	 *
	 * @param text           被检查的文本
	 * @param limit          限制匹配个数，如果小于等于0，则返回全部匹配结果
	 * @param isDensityMatch 是否使用密集匹配原则
	 * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
	 * @return 匹配的词列表
	 */
	public List<String> matchAll(final String text, final int limit, final boolean isDensityMatch, final boolean isGreedMatch) {
		final List<FoundWord> matchAllWords = matchAllWords(text, limit, isDensityMatch, isGreedMatch);
		return CollUtil.map(matchAllWords, FoundWord::toString);
	}

	/**
	 * 找出所有匹配的关键字<br>
	 * <p>假如被检查文本是{@literal "abab"}<br>
	 * 密集匹配原则：假如关键词有 ab,b，将匹配 [ab,b,ab,b]<br>
	 * 贪婪匹配（最长匹配）原则：假如关键字a,ab，最长匹配将匹配[ab]
	 * </p>
	 *
	 * @param text           被检查的文本
	 * @param limit          限制匹配个数，如果小于等于0，则返回全部匹配结果
	 * @param isDensityMatch 是否使用密集匹配原则
	 * @param isGreedMatch   是否使用贪婪匹配（最长匹配）原则
	 * @return 匹配的词列表
	 * @since 5.5.3
	 */
	public List<FoundWord> matchAllWords(final String text, final int limit, final boolean isDensityMatch, final boolean isGreedMatch) {
		if (null == text) {
			return null;
		}

		final List<FoundWord> foundWords = limit > 0 ? new ArrayList<>(limit) : new ArrayList<>();
		WordTree current;
		final int length = text.length();
		final Predicate<Character> charFilter = this.charFilter;
		//存放查找到的字符缓存。完整出现一个词时加到foundWords中，否则清空
		final StringBuilder wordBuffer = StrUtil.builder();
		final StringBuilder keyBuffer = StrUtil.builder();
		char currentChar;
		for (int i = 0; i < length; i++) {
			current = this;
			wordBuffer.setLength(0);
			keyBuffer.setLength(0);

			// 单次匹配，每次循环最多匹配一个词
			FoundWord currentFoundWord = null;
			for (int j = i; j < length; j++) {
				currentChar = text.charAt(j);
				if (!charFilter.test(currentChar)) {
					if (wordBuffer.length() > 0) {
						//做为关键词中间的停顿词被当作关键词的一部分被返回
						wordBuffer.append(currentChar);
					} else {
						//停顿词做为关键词的第一个字符时需要跳过
						i++;
					}
					continue;
				} else if (!current.containsKey(currentChar)) {
					// 节点不匹配，开始下一轮
					break;
				}
				wordBuffer.append(currentChar);
				keyBuffer.append(currentChar);
				if (current.isEnd(currentChar)) {
					//到达单词末尾，关键词成立，从此词的下一个位置开始查找
					currentFoundWord = new FoundWord(keyBuffer.toString(), wordBuffer.toString(), i, j);
					//如果非密度匹配，跳过匹配到的词
					if (!isDensityMatch) {
						i = j;
					}

					//如果非贪婪匹配。当遇到第一个结尾标记就结束本轮匹配
					if (!isGreedMatch) {
						break;
					}
				}
				// 查找下一个节点，节点始终不会为null，因为当前阶段或匹配结束，或匹配不到结束
				current = current.get(currentChar);
			}

			// 本次循环结尾，加入遗留匹配的单词
			if(null != currentFoundWord){
				foundWords.add(currentFoundWord);
				if (limit > 0 && foundWords.size() >= limit) {
					//超过匹配限制个数，直接返回
					return foundWords;
				}
			}
		}
		return foundWords;
	}

	/**
	 * 扁平化WordTree
	 * 例如：红领巾，红河 构建树后为：
	 * <pre>
	 *            红
	 *            /\
	 *          领  河
	 *         /
	 *       巾
	 * </pre>
	 * 扁平化后得到
	 * <pre>
	 *     红河
	 *     红领巾
	 * </pre>
	 *
	 * @return 扁平化后的结果，不保证顺序
	 */
	public List<String> flatten() {
		return EasyStream.of(this.entrySet()).flat(this::innerFlatten).toList();
	}

	//--------------------------------------------------------------------------------------- Private method start

	/**
	 * 递归扁平化WordTree每个entry节点
	 *
	 * @param entry WordTree每个entry节点
	 * @return 递归扁平化后的结果
	 */
	private Iterable<String> innerFlatten(Entry<Character, WordTree> entry) {
		List<String> list = EasyStream.of(entry.getValue().entrySet()).flat(this::innerFlatten).map(v -> entry.getKey() + v).toList();
		if (list.isEmpty()) {
			return EasyStream.of(entry.getKey().toString());
		}
		return list;
	}

	/**
	 * 是否末尾
	 *
	 * @param c 检查的字符
	 * @return 是否末尾
	 */
	private boolean isEnd(final char c) {
		return null != endCharacterSet && this.endCharacterSet.contains(c);
	}

	/**
	 * 设置已到达末尾
	 *
	 * @param c 设置结尾的字符
	 */
	private void setEnd(final char c) {
		if (null == endCharacterSet) {
			// 叶子节点一般也就1个元素
			endCharacterSet = new HashSet<>(2);
		}
		this.endCharacterSet.add(c);
	}

	/**
	 * 清除所有的词,
	 * 此方法调用后, wordTree 将被清空
	 * endCharacterSet 也将清空
	 */
	@Override
	public void clear() {
		super.clear();
		if (null != endCharacterSet) {
			this.endCharacterSet.clear();
		}
	}
	//--------------------------------------------------------------------------------------- Private method end
}
