/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
