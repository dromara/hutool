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

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dromara.hutool.poi.excel.cell.editors.CellEditor;
import org.dromara.hutool.poi.excel.cell.CellUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取单独一列
 *
 * @author looly
 * @since 5.7.17
 */
public class ColumnSheetReader extends AbstractSheetReader<List<Object>> {

	/**
	 * 构造
	 *
	 * @param columnIndex   列号，从0开始计数
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public ColumnSheetReader(final int columnIndex, final int startRowIndex, final int endRowIndex) {
		super(new CellRangeAddress(startRowIndex, endRowIndex, columnIndex, columnIndex));
	}

	@Override
	public List<Object> read(final Sheet sheet) {
		final List<Object> resultList = new ArrayList<>();

		final int startRowIndex = Math.max(this.cellRangeAddress.getFirstRow(), sheet.getFirstRowNum());// 读取起始行（包含）
		final int endRowIndex = Math.min(this.cellRangeAddress.getLastRow(), sheet.getLastRowNum());// 读取结束行（包含）
		final int columnIndex = this.cellRangeAddress.getFirstColumn();

		final CellEditor cellEditor = this.config.getCellEditor();
		final boolean ignoreEmptyRow = this.config.isIgnoreEmptyRow();
		Object value;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			value = CellUtil.getCellValue(CellUtil.getCell(sheet.getRow(i), columnIndex), cellEditor);
			if(null != value || !ignoreEmptyRow){
				resultList.add(value);
			}
		}

		return resultList;
	}
}
