package cn.hutool.extra.tokenizer.engine.mynlp;

import com.mayabot.nlp.segment.WordTerm;

import cn.hutool.extra.tokenizer.Word;

/**
 * mmseg分词中的一个单词包装
 *
 * @author looly
 */
public class MynlpWord implements Word {
	private static final long serialVersionUID = 1L;

	private final WordTerm word;

	/**
	 * 构造
	 *
	 * @param word {@link WordTerm}
	 */
	public MynlpWord(WordTerm word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getWord();
	}

	@Override
	public int getStartOffset() {
		return this.word.offset;
	}

	@Override
	public int getEndOffset() {
		return getStartOffset() + word.word.length();
	}

	@Override
	public String toString() {
		return getText();
	}
}
