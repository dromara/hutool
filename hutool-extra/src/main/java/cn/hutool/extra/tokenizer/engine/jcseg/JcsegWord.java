package cn.hutool.extra.tokenizer.engine.jcseg;

import cn.hutool.extra.tokenizer.Word;
import org.lionsoul.jcseg.IWord;

/**
 * Jcseg分词中的一个单词包装
 * 
 * @author looly
 *
 */
public class JcsegWord implements Word {
	private static final long serialVersionUID = 1L;

	private final IWord word;

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
	public int getStartOffset() {
		return word.getPosition();
	}
	
	@Override
	public int getEndOffset() {
		return getStartOffset() + word.getLength();
	}

	@Override
	public String toString() {
		return getText();
	}
}
