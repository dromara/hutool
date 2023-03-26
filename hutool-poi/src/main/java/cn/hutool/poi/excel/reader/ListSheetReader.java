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

package cn.hutool.poi.excel.reader;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import org.apache.poi.ss.usermodel.Sheet;

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

		final int startRowIndex = Math.max(this.startRowIndex, sheet.getFirstRowNum());// 读取起始行（包含）
		final int endRowIndex = Math.min(this.endRowIndex, sheet.getLastRowNum());// 读取结束行（包含）
		List<Object> rowList;
		for (int i = startRowIndex; i <= endRowIndex; i++) {
			rowList = readRow(sheet, i);
			if (CollUtil.isNotEmpty(rowList) || false == ignoreEmptyRow) {
				if (aliasFirstLine && i == startRowIndex) {
					// 第一行作为标题行，替换别名
					rowList = Convert.toList(Object.class, aliasHeader(rowList));
				}
				resultList.add(rowList);
			}
		}
		return resultList;
	}
}
