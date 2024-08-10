/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.poi.excel.reader.sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.core.func.SerBiConsumer;
import org.dromara.hutool.poi.excel.cell.CellEditor;
import org.dromara.hutool.poi.excel.cell.CellUtil;

/**
 * 读取Excel的Sheet，使用Consumer方式处理单元格
 *
 * @author Looly
 * @since 6.0.0
 */
public class WalkSheetReader extends AbstractSheetReader<Void> {


	private final SerBiConsumer<Cell, Object> cellHandler;

	/**
	 * 构造
	 *
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 * @param cellHandler   单元格处理器，用于处理读到的单元格及其数据
	 */
	public WalkSheetReader(final int startRowIndex, final int endRowIndex, final SerBiConsumer<Cell, Object> cellHandler) {
		super(startRowIndex, endRowIndex);
		this.cellHandler = cellHandler;
	}

	@Override
	public Void read(final Sheet sheet) {
		final int startRowIndex = Math.max(this.cellRangeAddress.getFirstRow(), sheet.getFirstRowNum());// 读取起始行（包含）
		final int endRowIndex = Math.min(this.cellRangeAddress.getLastRow(), sheet.getLastRowNum());// 读取结束行（包含）
		final CellEditor cellEditor = this.config.getCellEditor();

		Row row;
		for (int y = startRowIndex; y <= endRowIndex; y++) {
			row = sheet.getRow(y);
			if (null != row) {
				final short startColumnIndex = (short) Math.max(this.cellRangeAddress.getFirstColumn(), row.getFirstCellNum());
				final short endColumnIndex = (short) Math.min(this.cellRangeAddress.getLastColumn(), row.getLastCellNum());
				Cell cell;
				for (short x = startColumnIndex; x < endColumnIndex; x++) {
					cell = CellUtil.getCell(row, x);
					cellHandler.accept(cell, CellUtil.getCellValue(cell, cellEditor));
				}
			}
		}

		return null;
	}
}
