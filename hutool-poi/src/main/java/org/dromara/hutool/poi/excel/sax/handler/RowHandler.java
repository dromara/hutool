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

package org.dromara.hutool.poi.excel.sax.handler;

import org.apache.poi.ss.usermodel.CellStyle;

import java.util.List;

/**
 * Sax方式读取Excel行处理器
 *
 * @author looly
 */
@FunctionalInterface
public interface RowHandler {

	/**
	 * 处理一行数据
	 *
	 * @param sheetIndex 当前Sheet序号
	 * @param rowIndex   当前行号，从0开始计数
	 * @param rowCells   行数据，每个Object表示一个单元格的值
	 */
	void handle(int sheetIndex, long rowIndex, List<Object> rowCells);

	/**
	 * 处理一个单元格的数据
	 *
	 * @param sheetIndex    当前Sheet序号
	 * @param rowIndex      当前行号
	 * @param cellIndex     当前列号
	 * @param value         单元格的值
	 * @param xssfCellStyle 单元格样式
	 */
	default void handleCell(final int sheetIndex, final long rowIndex, final int cellIndex, final Object value, final CellStyle xssfCellStyle) {
		//pass
	}

	/**
	 * 处理一个sheet页完成的操作
	 */
	default void doAfterAllAnalysed() {
		//pass
	}
}
