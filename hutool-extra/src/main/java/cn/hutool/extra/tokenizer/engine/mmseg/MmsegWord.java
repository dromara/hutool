package cn.hutool.extra.tokenizer.engine.mmseg;

import cn.hutool.extra.tokenizer.Word;

/**
 * mmseg分词中的一个单词包装
 * 
 * @author looly
 *
 */
public class MmsegWord implements Word {
	private static final long serialVersionUID = 1L;
	
	private final com.chenlb.mmseg4j.Word word;

	/**
	 * 构造
	 * 
	 * @param word {@link com.chenlb.mmseg4j.Word}
	 */
	public MmsegWord(com.chenlb.mmseg4j.Word word) {
		this.word = word;
	}

	@Override
	public String getText() {
		return word.getString();
	}
	
	@Override
	public int getStartOffset() {
		return this.word.getStartOffset();
	}
	
	@Override
	public int getEndOffset() {
		return this.word.getEndOffset();
	}

	@Override
	public String toString() {
		return getText();
	}
}
