package cn.hutool.extra.tokenizer.engine.jieba;

import com.huaban.analysis.jieba.SegToken;

import cn.hutool.extra.tokenizer.Word;

/**
 * Jieba分词中的一个单词包装
 * 
 * @author looly
 *
 */
public class JiebaWord implements Word {
	private SegToken segToken;

	/**
	 * 构造
	 * 
	 * @param segToken {@link SegToken}
	 */
	public JiebaWord(SegToken segToken) {
		this.segToken = segToken;
	}

	@Override
	public String getText() {
		return segToken.word;
	}

	@Override
	public String toString() {
		return getText();
	}
}
