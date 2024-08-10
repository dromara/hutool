/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.reader;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dromara.hutool.poi.excel.ExcelConfig;

/**
 * 抽象{@link Sheet}数据读取实现
 *
 * @param <T> 读取类型
 * @author looly
 * @since 5.4.4
 */
public abstract class AbstractSheetReader<T> implements SheetReader<T> {

	protected final CellRangeAddress cellRangeAddress;
	/**
	 * 是否忽略空行
	 */
	protected boolean ignoreEmptyRow = true;
	/**
	 * Excel配置
	 */
	protected ExcelConfig config;

	/**
	 * 构造
	 *
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public AbstractSheetReader(final int startRowIndex, final int endRowIndex) {
		this(new CellRangeAddress(
			Math.min(startRowIndex, endRowIndex),
			Math.max(startRowIndex, endRowIndex),
			0, Integer.MAX_VALUE));
	}

	/**
	 * 构造
	 *
	 * @param cellRangeAddress 读取范围
	 */
	public AbstractSheetReader(final CellRangeAddress cellRangeAddress) {
		this.cellRangeAddress = cellRangeAddress;
	}

	/**
	 * 设置Excel配置
	 *
	 * @param config Excel配置
	 */
	public void setExcelConfig(final ExcelConfig config) {
		this.config = config;
	}

	/**
	 * 设置是否忽略空行
	 *
	 * @param ignoreEmptyRow 是否忽略空行
	 */
	public void setIgnoreEmptyRow(final boolean ignoreEmptyRow) {
		this.ignoreEmptyRow = ignoreEmptyRow;
	}
}
