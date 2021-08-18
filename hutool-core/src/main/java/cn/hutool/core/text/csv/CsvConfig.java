package cn.hutool.core.text.csv;

import cn.hutool.core.util.CharUtil;

import java.io.Serializable;

/**
 * CSV基础配置项，此配置项可用于读取和写出CSV，定义了包括字段分隔符、文本包装符等符号
 *
 * @param <T> 继承子类类型，用于this返回
 * @author looly
 * @since 4.0.5
 */
public class CsvConfig<T extends CsvConfig<?>> implements Serializable {
	private static final long serialVersionUID = -8069578249066158459L;

	/**
	 * 字段分隔符，默认逗号','
	 */
	protected char fieldSeparator = CharUtil.COMMA;
	/**
	 * 文本包装符，默认双引号'"'
	 */
	protected char textDelimiter = CharUtil.DOUBLE_QUOTES;
	/**
	 * 注释符号，用于区分注释行，默认'#'
	 */
	protected char commentCharacter = '#';

	/**
	 * 设置字段分隔符，默认逗号','
	 *
	 * @param fieldSeparator 字段分隔符，默认逗号','
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T setFieldSeparator(final char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
		return (T) this;
	}

	/**
	 * 设置 文本分隔符，文本包装符，默认双引号'"'
	 *
	 * @param textDelimiter 文本分隔符，文本包装符，默认双引号'"'
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T setTextDelimiter(char textDelimiter) {
		this.textDelimiter = textDelimiter;
		return (T) this;
	}

	/**
	 * 设置 注释符号，用于区分注释行
	 *
	 * @param commentCharacter 注释符号，用于区分注释行
	 * @return this
	 * @since 5.5.7
	 */
	@SuppressWarnings("unchecked")
	public T setCommentCharacter(char commentCharacter) {
		this.commentCharacter = commentCharacter;
		return (T) this;
	}
}
