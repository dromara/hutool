package cn.hutool.dfa;

/**
 * @author 肖海斌
 * <p>
 * 匹配到的敏感词，包含敏感词，text中匹配敏感词的内容，以及匹配内容在text中的下标，
 * 下标可以用来做敏感词的进一步处理，如果替换成**
 */
public class FoundWord {
	/**
	 * 生效的敏感词
	 */
	private String word;
	/**
	 * 敏感词匹配到的内容
	 */
	private String foundWord;
	/**
	 * 匹配内容在待分析字符串中的开始位置
	 */
	private int startIndex;
	/**
	 * 匹配内容在待分析字符串中的结束位置
	 */
	private int endIndex;

	public FoundWord(String word, String foundWord, int start, int end) {
		this.word = word;
		this.foundWord = foundWord;
		this.startIndex = start;
		this.endIndex = end;
	}

	public String getWord() {
		return word;
	}

	public String getFoundWord() {
		return foundWord;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}
}
