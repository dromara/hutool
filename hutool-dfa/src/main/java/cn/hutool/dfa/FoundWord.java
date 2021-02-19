package cn.hutool.dfa;

import cn.hutool.core.lang.DefaultSegment;

/**
 * <p>
 * 匹配到的单词，包含单词，text中匹配单词的内容，以及匹配内容在text中的下标，
 * 下标可以用来做单词的进一步处理，如果替换成**
 *
 * @author 肖海斌
 */
public class FoundWord extends DefaultSegment<Integer> {
	/**
	 * 生效的单词，即单词树中的词
	 */
	private final String word;
	/**
	 * 单词匹配到的内容，即文中的单词
	 */
	private final String foundWord;

	/**
	 * 构造
	 *
	 * @param word 生效的单词，即单词树中的词
	 * @param foundWord 单词匹配到的内容，即文中的单词
	 * @param startIndex 起始位置（包含）
	 * @param endIndex 结束位置（包含）
	 */
	public FoundWord(String word, String foundWord, int startIndex, int endIndex) {
		super(startIndex, endIndex);
		this.word = word;
		this.foundWord = foundWord;
	}

	/**
	 * 获取生效的单词，即单词树中的词
	 *
	 * @return 生效的单词
	 */
	public String getWord() {
		return word;
	}

	/**
	 * 获取单词匹配到的内容，即文中的单词
	 * @return 单词匹配到的内容
	 */
	public String getFoundWord() {
		return foundWord;
	}

	/**
	 * 默认的，只输出匹配到的关键字
	 * @return 匹配到的关键字
	 */
	@Override
	public String toString() {
		return this.foundWord;
	}
}
