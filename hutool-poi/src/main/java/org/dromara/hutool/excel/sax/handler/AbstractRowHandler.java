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

import org.dromara.hutool.lang.Assert;

import java.util.List;
import java.util.function.Function;

/**
 * 抽象行数据处理器，通过实现{@link #handle(int, long, List)} 处理原始数据<br>
 * 并调用{@link #handleData(int, long, Object)}处理经过转换后的数据。
 *
 * @param <T> 转换后的数据类型
 * @author looly
 * @since 5.4.4
 */
public abstract class AbstractRowHandler<T> implements RowHandler {

	/**
	 * 读取起始行（包含，从0开始计数）
	 */
	protected final int startRowIndex;
	/**
	 * 读取结束行（包含，从0开始计数）
	 */
	protected final int endRowIndex;
	/**
	 * 行数据转换函数
	 */
	protected Function<List<Object>, T> convertFunc;

	/**
	 * 构造
	 *
	 * @param startRowIndex 读取起始行（包含，从0开始计数）
	 * @param endRowIndex 读取结束行（包含，从0开始计数）
	 */
	public AbstractRowHandler(final int startRowIndex, final int endRowIndex) {
		this.startRowIndex = startRowIndex;
		this.endRowIndex = endRowIndex;
	}

	@Override
	public void handle(final int sheetIndex, final long rowIndex, final List<Object> rowCells) {
		Assert.notNull(convertFunc);
		if (rowIndex < this.startRowIndex || rowIndex > this.endRowIndex) {
			return;
		}
		handleData(sheetIndex, rowIndex, convertFunc.apply(rowCells));
	}

	/**
	 * 处理转换后的数据
	 *
	 * @param sheetIndex 当前Sheet序号
	 * @param rowIndex   当前行号，从0开始计数
	 * @param data       行数据
	 */
	public abstract void handleData(int sheetIndex, long rowIndex, T data);
}
