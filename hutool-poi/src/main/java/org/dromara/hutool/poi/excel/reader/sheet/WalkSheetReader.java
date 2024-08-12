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

package org.dromara.hutool.poi.excel.reader.sheet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.core.func.SerBiConsumer;
import org.dromara.hutool.poi.excel.cell.editors.CellEditor;
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
