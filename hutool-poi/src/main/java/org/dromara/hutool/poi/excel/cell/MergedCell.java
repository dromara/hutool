/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.poi.excel.cell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * 合并单元格封装
 *
 * @author Looly
 * @since 6.0.0
 */
public class MergedCell {

	/**
	 * 创建MergedCell
	 *
	 * @param cell        第一个单元格，即左上角的单元格
	 * @param rowCount    占用行数
	 * @param columnCount 占用列数
	 * @return MergedCell
	 */
	public static MergedCell of(final Cell cell, final int rowCount, final int columnCount) {
		final int rowIndex = cell.getRowIndex();
		final int columnIndex = cell.getColumnIndex();
		return of(cell, new CellRangeAddress(
			rowIndex, rowIndex + rowCount - 1,
			columnIndex, columnIndex + columnCount - 1));
	}

	/**
	 * 创建MergedCell
	 *
	 * @param cell  第一个单元格，即左上角的单元格
	 * @param range 合并单元格范围
	 * @return MergedCell
	 */
	public static MergedCell of(final Cell cell, final CellRangeAddress range) {
		return new MergedCell(cell, range);
	}

	private final Cell first;
	private final CellRangeAddress range;

	/**
	 * 构造
	 *
	 * @param first  第一个单元格，即左上角的单元格
	 * @param range 合并单元格范围
	 */
	public MergedCell(final Cell first, final CellRangeAddress range) {
		this.first = first;
		this.range = range;
	}

	/**
	 * 获取第一个单元格，即左上角的单元格
	 *
	 * @return Cell
	 */
	public Cell getFirst() {
		return this.first;
	}

	/**
	 * 获取合并单元格范围
	 *
	 * @return CellRangeAddress
	 */
	public CellRangeAddress getRange() {
		return this.range;
	}

	/**
	 * 设置单元格样式
	 *
	 * @param cellStyle 单元格样式
	 * @return this
	 */
	public MergedCell setCellStyle(final CellStyle cellStyle) {
		this.first.setCellStyle(cellStyle);
		return this;
	}

	/**
	 * 设置单元格值
	 *
	 * @param value 值
	 * @return this
	 */
	public MergedCell setValue(final Object value) {
		CellUtil.setCellValue(this.first, value);
		return this;
	}
}
