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

package org.dromara.hutool.excel.sax.handler;

import org.dromara.hutool.collection.iter.IterUtil;
import org.dromara.hutool.collection.ListUtil;
import org.dromara.hutool.convert.Convert;

import java.util.List;
import java.util.Map;

/**
 * Map形式的行处理器<br>
 * 将一行数据转换为Map，key为指定行，value为当前行对应位置的值
 *
 * @author looly
 * @since 5.4.4
 */
public abstract class MapRowHandler extends AbstractRowHandler<Map<String, Object>> {

	/**
	 * 标题所在行（从0开始计数）
	 */
	private final int headerRowIndex;
	/**
	 * 标题行
	 */
	List<String> headerList;

	/**
	 * 构造
	 *
	 * @param headerRowIndex 标题所在行（从0开始计数）
	 * @param startRowIndex 读取起始行（包含，从0开始计数）
	 * @param endRowIndex 读取结束行（包含，从0开始计数）
	 */
	public MapRowHandler(final int headerRowIndex, final int startRowIndex, final int endRowIndex){
		super(startRowIndex, endRowIndex);
		this.headerRowIndex = headerRowIndex;
		this.convertFunc = (rowList)-> IterUtil.toMap(headerList, rowList);
	}

	@Override
	public void handle(final int sheetIndex, final long rowIndex, final List<Object> rowCells) {
		if (rowIndex == this.headerRowIndex) {
			this.headerList = ListUtil.view(Convert.toList(String.class, rowCells));
			return;
		}
		super.handle(sheetIndex, rowIndex, rowCells);
	}
}
