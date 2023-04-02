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

package org.dromara.hutool.excel.reader;

import org.dromara.hutool.collection.CollUtil;
import org.dromara.hutool.collection.iter.IterUtil;
import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.text.StrUtil;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 读取{@link Sheet}为Map的List列表形式
 *
 * @author looly
 * @since 5.4.4
 */
public class MapSheetReader extends AbstractSheetReader<List<Map<String, Object>>> {

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
	public List<Map<String, Object>> read(final Sheet sheet) {
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
		} else if (startRowIndex > lastRowNum) {
			// issue#I5U1JA 只有标题行的Excel，起始行是1，标题行（最后的行号是0）
			return ListUtil.empty();
		}
		final int startRowIndex = Math.max(this.startRowIndex, firstRowNum);// 读取起始行（包含）
		final int endRowIndex = Math.min(this.endRowIndex, lastRowNum);// 读取结束行（包含）

		// 读取header
		final List<String> headerList = aliasHeader(readRow(sheet, headerRowIndex));

		final List<Map<String, Object>> result = new ArrayList<>(endRowIndex - startRowIndex + 1);
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			// 跳过标题行
			if (i != headerRowIndex) {
				rowList = readRow(sheet, i);
				if (CollUtil.isNotEmpty(rowList) || false == ignoreEmptyRow) {
					result.add(IterUtil.toMap(headerList, rowList, true));
				}
			}
		}
		return result;
	}
}
