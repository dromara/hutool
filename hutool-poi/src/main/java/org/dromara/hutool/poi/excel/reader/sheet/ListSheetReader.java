/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.convert.Convert;
import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.poi.excel.RowUtil;
import org.dromara.hutool.poi.excel.cell.editors.CellEditor;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取{@link Sheet}为List列表形式
 *
 * @author looly
 * @since 5.4.4
 */
public class ListSheetReader extends AbstractSheetReader<List<List<Object>>> {

	/** 是否首行作为标题行转换别名 */
	private final boolean aliasFirstLine;

	/**
	 * 构造
	 *
	 * @param startRowIndex  起始行（包含，从0开始计数）
	 * @param endRowIndex    结束行（包含，从0开始计数）
	 * @param aliasFirstLine 是否首行作为标题行转换别名
	 */
	public ListSheetReader(final int startRowIndex, final int endRowIndex, final boolean aliasFirstLine) {
		super(startRowIndex, endRowIndex);
		this.aliasFirstLine = aliasFirstLine;
	}

	@Override
	public List<List<Object>> read(final Sheet sheet) {
		final List<List<Object>> resultList = new ArrayList<>();

		final int startRowIndex = Math.max(this.cellRangeAddress.getFirstRow(), sheet.getFirstRowNum());// 读取起始行（包含）
		final int endRowIndex = Math.min(this.cellRangeAddress.getLastRow(), sheet.getLastRowNum());// 读取结束行（包含）

		List<Object> rowList;
		final CellEditor cellEditor = this.config.getCellEditor();
		final boolean ignoreEmptyRow = this.config.isIgnoreEmptyRow();
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			rowList = RowUtil.readRow(sheet.getRow(i), cellEditor);
			if (CollUtil.isNotEmpty(rowList) || !ignoreEmptyRow) {
				if (aliasFirstLine && i == startRowIndex) {
					// 第一行作为标题行，替换别名
					rowList = this.config.aliasHeader(rowList);
				}
				resultList.add(rowList);
			}
		}
		return resultList;
	}
}
