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
import org.dromara.hutool.core.collection.iter.IterUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.dromara.hutool.poi.excel.RowUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取{@link Sheet}为Map的List列表形式
 *
 * @author looly
 * @since 5.4.4
 */
public class MapSheetReader extends AbstractSheetReader<List<Map<Object, Object>>> {

	private final int headerRowIndex;

	/**
	 * 构造
	 *
	 * @param headerRowIndex 标题所在行，如果标题行在读取的内容行中间，这行做为数据将忽略
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public MapSheetReader(final int headerRowIndex, final int startRowIndex, final int endRowIndex) {
		super(startRowIndex, endRowIndex);
		this.headerRowIndex = headerRowIndex;
	}

	@Override
	public List<Map<Object, Object>> read(final Sheet sheet) {
		// 边界判断
		final int firstRowNum = sheet.getFirstRowNum();
		final int lastRowNum = sheet.getLastRowNum();
		if(lastRowNum < 0){
			return ListUtil.empty();
		}

		if (headerRowIndex < firstRowNum) {
			throw new IndexOutOfBoundsException(StrUtil.format("Header row index {} is lower than first row index {}.", headerRowIndex, firstRowNum));
		} else if (headerRowIndex > lastRowNum) {
			throw new IndexOutOfBoundsException(StrUtil.format("Header row index {} is greater than last row index {}.", headerRowIndex, lastRowNum));
		}

		int startRowIndex = this.cellRangeAddress.getFirstRow();
		if (startRowIndex > lastRowNum) {
			// issue#I5U1JA 只有标题行的Excel，起始行是1，标题行（最后的行号是0）
			return ListUtil.empty();
		}
		startRowIndex = Math.max(startRowIndex, firstRowNum);// 读取起始行（包含）
		final int endRowIndex = Math.min(this.cellRangeAddress.getLastRow(), lastRowNum);// 读取结束行（包含）

		// 读取header
		final List<Object> headerList = this.config.aliasHeader(readRow(sheet, headerRowIndex));

		final List<Map<Object, Object>> result = new ArrayList<>(endRowIndex - startRowIndex + 1);
		final boolean ignoreEmptyRow = this.config.isIgnoreEmptyRow();
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			// 跳过标题行
			if (i != headerRowIndex) {
				rowList = readRow(sheet, i);
				if (CollUtil.isNotEmpty(rowList) || !ignoreEmptyRow) {
					result.add(IterUtil.toMap(headerList, rowList, true));
				}
			}
		}
		return result;
	}

	/**
	 * 读取某一行数据
	 *
	 * @param sheet {@link Sheet}
	 * @param rowIndex 行号，从0开始
	 * @return 一行数据
	 */
	private List<Object> readRow(final Sheet sheet, final int rowIndex) {
		return RowUtil.readRow(sheet.getRow(rowIndex), this.config.getCellEditor());
	}
}
