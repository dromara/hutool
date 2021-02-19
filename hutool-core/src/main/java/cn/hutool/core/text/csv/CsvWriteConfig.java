package cn.hutool.core.text.csv;

import java.io.Serializable;

import cn.hutool.core.util.CharUtil;

/**
 * CSV写出配置项
 * 
 * @author looly
 *
 */
public class CsvWriteConfig extends CsvConfig implements Serializable {
	private static final long serialVersionUID = 5396453565371560052L;

	/** 是否始终使用文本分隔符，文本包装符，默认false，按需添加 */
	protected boolean alwaysDelimitText;
	/** 换行符 */
	protected char[] lineDelimiter = {CharUtil.CR, CharUtil.LF};

	/**
	 * 默认配置
	 * 
	 * @return 默认配置
	 */
	public static CsvWriteConfig defaultConfig() {
		return new CsvWriteConfig();
	}

	/**
	 * 设置是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 * 
	 * @param alwaysDelimitText 是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 */
	public void setAlwaysDelimitText(boolean alwaysDelimitText) {
		this.alwaysDelimitText = alwaysDelimitText;
	}
	
	/**
	 * 设置换行符
	 * 
	 * @param lineDelimiter 换行符
	 */
	public void setLineDelimiter(char[] lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}
}
