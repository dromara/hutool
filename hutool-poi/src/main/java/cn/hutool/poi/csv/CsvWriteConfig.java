package cn.hutool.poi.csv;

import cn.hutool.core.util.CharUtil;

import java.io.Serializable;

/**
 * CSV写出配置项
 *
 * @author looly
 */
public class CsvWriteConfig extends CsvConfig<CsvWriteConfig> implements Serializable {
	private static final long serialVersionUID = 5396453565371560052L;

	/**
	 * 是否始终使用文本分隔符，文本包装符，默认false，按需添加
	 */
	protected boolean alwaysDelimitText;
	/**
	 * 换行符
	 */
	protected char[] lineDelimiter = {CharUtil.CR, CharUtil.LF};
	/**
	 * 是否使用安全模式，对可能存在DDE攻击的内容进行替换
	 */
	protected boolean  ddeSafe;

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
	 * @return this
	 */
	public CsvWriteConfig setAlwaysDelimitText(final boolean alwaysDelimitText) {
		this.alwaysDelimitText = alwaysDelimitText;
		return this;
	}

	/**
	 * 设置换行符
	 *
	 * @param lineDelimiter 换行符
	 * @return this
	 */
	public CsvWriteConfig setLineDelimiter(final char[] lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
		return this;
	}

	/**
	 * 设置是否动态数据交换安全，使用文本包装符包裹可能存在DDE攻击的内容
	 *
	 * @param ddeSafe dde安全
	 * @return this
	 */
	public CsvWriteConfig setDdeSafe(final boolean ddeSafe){
		this.ddeSafe = ddeSafe;
		return this;
	}

}
