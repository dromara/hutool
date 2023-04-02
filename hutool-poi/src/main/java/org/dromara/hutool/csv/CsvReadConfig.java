/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.csv;

import java.io.Serializable;

/**
 * CSV读取配置项
 *
 * @author looly
 *
 */
public class CsvReadConfig extends CsvConfig<CsvReadConfig> implements Serializable {
	private static final long serialVersionUID = 5396453565371560052L;

	/** 指定标题行号，-1表示无标题行 */
	protected long headerLineNo = -1;
	/** 是否跳过空白行，默认true */
	protected boolean skipEmptyRows = true;
	/** 每行字段个数不同时是否抛出异常，默认false */
	protected boolean errorOnDifferentFieldCount;
	/** 定义开始的行（包括），此处为原始文件行号 */
	protected long beginLineNo;
	/** 结束的行（包括），此处为原始文件行号 */
	protected long endLineNo = Long.MAX_VALUE-1;
	/** 每个字段是否去除两边空白符 */
	protected boolean trimField;

	/**
	 * 默认配置
	 *
	 * @return 默认配置
	 */
	public static CsvReadConfig defaultConfig() {
		return new CsvReadConfig();
	}

	/**
	 * 设置是否首行做为标题行，默认false<br>
	 * 当设置为{@code true}时，默认标题行号是{@link #beginLineNo}，{@code false}为-1，表示无行号
	 *
	 * @param containsHeader 是否首行做为标题行，默认false
	 * @return this
	 * @see #setHeaderLineNo(long)
	 */
	public CsvReadConfig setContainsHeader(final boolean containsHeader) {
		return setHeaderLineNo(containsHeader ? beginLineNo : -1);
	}

	/**
	 * 设置标题行行号，默认-1，表示无标题行<br>
	 *
	 * @param headerLineNo 标题行行号，-1表示无标题行
	 * @return this
	 * @since 5.7.23
	 */
	public CsvReadConfig setHeaderLineNo(final long headerLineNo) {
		this.headerLineNo = headerLineNo;
		return this;
	}

	/**
	 * 设置是否跳过空白行，默认true
	 *
	 * @param skipEmptyRows 是否跳过空白行，默认true
	 * @return this
	 */
	public CsvReadConfig setSkipEmptyRows(final boolean skipEmptyRows) {
		this.skipEmptyRows = skipEmptyRows;
		return this;
	}

	/**
	 * 设置每行字段个数不同时是否抛出异常，默认false
	 *
	 * @param errorOnDifferentFieldCount 每行字段个数不同时是否抛出异常，默认false
	 * @return this
	 */
	public CsvReadConfig setErrorOnDifferentFieldCount(final boolean errorOnDifferentFieldCount) {
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
	public CsvReadConfig setBeginLineNo(final long beginLineNo) {
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
	public CsvReadConfig setEndLineNo(final long endLineNo) {
		this.endLineNo = endLineNo;
		return this;
	}

	/**
	 * 设置每个字段是否去除两边空白符<br>
	 * 如果字段以{@link #textDelimiter}包围，则保留两边空格
	 *
	 * @param trimField 去除两边空白符
	 * @return this
	 * @since 5.7.13
	 */
	public CsvReadConfig setTrimField(final boolean trimField) {
		this.trimField = trimField;
		return this;
	}
}
