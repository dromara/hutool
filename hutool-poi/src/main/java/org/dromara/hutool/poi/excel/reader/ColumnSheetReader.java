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

package org.dromara.hutool.poi.excel.reader;

import org.dromara.hutool.poi.excel.cell.CellUtil;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取单独一列
 *
 * @author looly
 * @since 5.7.17
 */
public class ColumnSheetReader extends AbstractSheetReader<List<Object>> {

	private final int columnIndex;

	/**
	 * 构造
	 *
	 * @param columnIndex   列号，从0开始计数
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public ColumnSheetReader(final int columnIndex, final int startRowIndex, final int endRowIndex) {
		super(startRowIndex, endRowIndex);
		this.columnIndex = columnIndex;
	}

	@Override
	public List<Object> read(final Sheet sheet) {
		final List<Object> resultList = new ArrayList<>();

		final int startRowIndex = Math.max(this.startRowIndex, sheet.getFirstRowNum());// 读取起始行（包含）
		final int endRowIndex = Math.min(this.endRowIndex, sheet.getLastRowNum());// 读取结束行（包含）

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
