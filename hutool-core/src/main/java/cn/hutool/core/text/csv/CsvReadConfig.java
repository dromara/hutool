package cn.hutool.core.text.csv;

import java.io.Serializable;

/**
 * CSV读取配置项
 *
 * @author looly
 *
 */
public class CsvReadConfig extends CsvConfig implements Serializable {
	private static final long serialVersionUID = 5396453565371560052L;

	/** 是否首行做为标题行，默认false */
	protected boolean containsHeader;
	/** 是否跳过空白行，默认true */
	protected boolean skipEmptyRows = true;
	/** 每行字段个数不同时是否抛出异常，默认false */
	protected boolean errorOnDifferentFieldCount;
	/** 定义开始的行（包括），此处为原始文件行号 */
	protected long beginLineNo;
	/** 结束的行（包括），此处为原始文件行号 */
	protected long endLineNo = Long.MAX_VALUE-1;

	/**
	 * 默认配置
	 *
	 * @return 默认配置
	 */
	public static CsvReadConfig defaultConfig() {
		return new CsvReadConfig();
	}

	/**
	 * 设置是否首行做为标题行，默认false
	 *
	 * @param containsHeader 是否首行做为标题行，默认false
	 * @return this
	 */
	public CsvReadConfig setContainsHeader(boolean containsHeader) {
		this.containsHeader = containsHeader;
		return this;
	}

	/**
	 * 设置是否跳过空白行，默认true
	 *
	 * @param skipEmptyRows 是否跳过空白行，默认true
	 * @return this
	 */
	public CsvReadConfig setSkipEmptyRows(boolean skipEmptyRows) {
		this.skipEmptyRows = skipEmptyRows;
		return this;
	}

	/**
	 * 设置每行字段个数不同时是否抛出异常，默认false
	 *
	 * @param errorOnDifferentFieldCount 每行字段个数不同时是否抛出异常，默认false
	 * @return this
	 */
	public CsvReadConfig setErrorOnDifferentFieldCount(boolean errorOnDifferentFieldCount) {
		this.errorOnDifferentFieldCount = errorOnDifferentFieldCount;
		return this;
	}

	/**
	 * 设置开始的行（包括），默认0，此处为原始文件行号
	 *
	 * @param beginLineNo 开始的行号（包括）
	 * @return this
	 * @since 5.7.4
	 */
	public CsvReadConfig setBeginLineNo(long beginLineNo) {
		this.beginLineNo = beginLineNo;
		return this;
	}

	/**
	 * 设置结束的行（包括），默认不限制，此处为原始文件行号
	 *
	 * @param endLineNo 结束的行号（包括）
	 * @return this
	 * @since 5.7.4
	 */
	public CsvReadConfig setEndLineNo(long endLineNo) {
		this.endLineNo = endLineNo;
		return this;
	}
}
