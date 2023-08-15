/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.csv;

import org.dromara.hutool.core.collection.ListUtil;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * CSV数据，包括头部信息和行数据，参考：FastCSV
 *
 * @author Looly
 */
public class CsvData implements Iterable<CsvRow>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<String> header;
	private final List<CsvRow> rows;

	/**
	 * 构造
	 *
	 * @param header 头信息, 可以为null
	 * @param rows 行
	 */
	public CsvData(final List<String> header, final List<CsvRow> rows) {
		this.header = header;
		this.rows = rows;
	}

	/**
	 * 总行数
	 *
	 * @return 总行数
	 */
	public int getRowCount() {
		return this.rows.size();
	}

	/**
	 * 获取头信息列表，如果无头信息为{@code Null}，返回列表为只读列表
	 *
	 * @return the header row - might be {@code null} if no header exists
	 */
	public List<String> getHeader() {
		return ListUtil.unmodifiable(this.header);
	}

	/**
	 * 获取指定行，从0开始
	 *
	 * @param index 行号
	 * @return 行数据
	 * @throws IndexOutOfBoundsException if index is out of range
	 */
	public CsvRow getRow(final int index) {
		return this.rows.get(index);
	}

	/**
	 * 获取所有行
	 *
	 * @return 所有行
	 */
	public List<CsvRow> getRows() {
		return this.rows;
	}

	@Override
	public Iterator<CsvRow> iterator() {
		return this.rows.iterator();
	}

	@Override
	public String toString() {
		return "CsvData{" +
				"header=" + header +
				", rows=" + rows +
				'}';
	}
}
