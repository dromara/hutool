package cn.hutool.core.text.csv;

import java.io.Serializable;

import cn.hutool.core.util.CharUtil;

/**
 * CSV读取配置项
 * 
 * @author looly
 *
 */
public class CsvReadConfig implements Serializable{
	private static final long serialVersionUID = 5396453565371560052L;

	/** 字段分隔符，默认逗号',' */
	protected char fieldSeparator = CharUtil.COMMA;
	/** 文本分隔符，文本包装符，默认双引号'"' */
	protected char textDelimiter = CharUtil.DOUBLE_QUOTES;
	/** 是否首行做为标题行，默认false */
	protected boolean containsHeader;
	/** 是否跳过空白行，默认true */
	protected boolean skipEmptyRows = true;
	/** 每行字段个数不同时是否抛出异常，默认false */
	protected boolean errorOnDifferentFieldCount;
	
	/**
	 * 默认配置
	 * @return 默认配置
	 */
	public static CsvReadConfig defaultConfig() {
		return new CsvReadConfig();
	}

	/**
	 * 设置字段分隔符，默认逗号','
	 * 
	 * @param fieldSeparator 字段分隔符，默认逗号','
	 */
	public void setFieldSeparator(final char fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	/**
	 * 设置 文本分隔符，文本包装符，默认双引号'"'
	 * 
	 * @param textDelimiter 文本分隔符，文本包装符，默认双引号'"'
	 */
	public void setTextDelimiter(char textDelimiter) {
		this.textDelimiter = textDelimiter;
	}

	/**
	 * 设置是否首行做为标题行，默认false
	 * 
	 * @param containsHeader 是否首行做为标题行，默认false
	 */
	public void setContainsHeader(boolean containsHeader) {
		this.containsHeader = containsHeader;
	}

	/**
	 * 设置是否跳过空白行，默认true
	 * 
	 * @param skipEmptyRows 是否跳过空白行，默认true
	 */
	public void setSkipEmptyRows(boolean skipEmptyRows) {
		this.skipEmptyRows = skipEmptyRows;
	}

	/**
	 * 设置每行字段个数不同时是否抛出异常，默认false
	 * 
	 * @param errorOnDifferentFieldCount 每行字段个数不同时是否抛出异常，默认false
	 */
	public void setErrorOnDifferentFieldCount(boolean errorOnDifferentFieldCount) {
		this.errorOnDifferentFieldCount = errorOnDifferentFieldCount;
	}
}
