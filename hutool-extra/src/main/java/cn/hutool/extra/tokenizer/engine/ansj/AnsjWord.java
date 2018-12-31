package cn.hutool.extra.tokenizer.engine.ansj;

import org.ansj.domain.Term;

import cn.hutool.extra.tokenizer.Word;

/**
 * Ansj分词中的一个单词包装
 * 
 * @author looly
 *
 */
public class AnsjWord implements Word {
	private Term term;

	/**
	 * 构造
	 * 
	 * @param term {@link Term}
	 */
	public AnsjWord(Term term) {
		this.term = term;
	}

	@Override
	public String getText() {
		return term.getName();
	}

	@Override
	public String toString() {
		return getText();
	}
}
