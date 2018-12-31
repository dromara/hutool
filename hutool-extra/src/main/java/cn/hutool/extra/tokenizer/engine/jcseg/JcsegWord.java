package cn.hutool.extra.tokenizer.engine.jcseg;

import org.lionsoul.jcseg.tokenizer.core.IWord;

import cn.hutool.extra.tokenizer.Word;

/**
 * Jcseg分词中的一个单词包装
 * 
 * @author looly
 *
 */
public class JcsegWord implements Word {
	private IWord word;

	/**
	 * 构造
	 * 
	 * @param word {@link IWord}
	 */
	public JcsegWord(IWord word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getValue();
	}

	@Override
	public String toString() {
		return getText();
	}
}
