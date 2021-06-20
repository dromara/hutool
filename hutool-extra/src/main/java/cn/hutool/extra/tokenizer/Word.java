package cn.hutool.extra.tokenizer;

import java.io.Serializable;

/**
 * 表示分词中的一个词
 * 
 * @author looly
 *
 */
public interface Word extends Serializable {

	/**
	 * 获取单词文本
	 * 
	 * @return 单词文本
	 */
	String getText();

	/**
	 * 获取本词的起始位置
	 * 
	 * @return 起始位置
	 */
	int getStartOffset();

	/**
	 * 获取本词的结束位置
	 * 
	 * @return 结束位置
	 */
	int getEndOffset();
}
