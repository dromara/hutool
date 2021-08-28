package cn.hutool.core.text.csv;

import cn.hutool.core.util.CharUtil;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * CSV基础配置项，此配置项可用于读取和写出CSV，定义了包括字段分隔符、文本包装符等符号
 *
 * @param <T> 继承子类类型，用于this返回
 * @author looly
 * @since 4.0.5
 */
@SuppressWarnings("unchecked")
public class CsvConfig<T extends CsvConfig<T>> implements Serializable {
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
	 * 标题别名
	 */
	protected Map<String, String> headerAlias = new LinkedHashMap<>();

	/**
	 * 设置字段分隔符，默认逗号','
	 *
	 * @param fieldSeparator 字段分隔符，默认逗号','
	 * @return this
	 */
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
	public T setCommentCharacter(char commentCharacter) {
		this.commentCharacter = commentCharacter;
		return (T) this;
	}

	/**
	 * 设置标题行的别名Map
	 *
	 * @param headerAlias 别名Map
	 * @return this
	 * @since 5.7.10
	 */
	public T setHeaderAlias(Map<String, String> headerAlias) {
		this.headerAlias = headerAlias;
		return (T) this;
	}

	/**
	 * 增加标题别名
	 *
	 * @param header 标题
	 * @param alias  别名
	 * @return this
	 * @since 5.7.10
	 */
	public T addHeaderAlias(String header, String alias) {
		this.headerAlias.put(header, alias);
		return (T) this;
	}

	/**
	 * 去除标题别名
	 *
	 * @param header 标题
	 * @return this
	 * @since 5.7.10
	 */
	public T removeHeaderAlias(String header) {
		this.headerAlias.remove(header);
		return (T) this;
	}
}
