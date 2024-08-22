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

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dromara.hutool.poi.excel.reader.ExcelReadConfig;

/**
 * 抽象{@link Sheet}数据读取实现
 *
 * @param <T> 读取类型
 * @author looly
 * @since 5.4.4
 */
public abstract class AbstractSheetReader<T> implements SheetReader<T> {

	protected final CellRangeAddress cellRangeAddress;
	/**
	 * Excel配置
	 */
	protected ExcelReadConfig config;

	/**
	 * 构造
	 *
	 * @param startRowIndex 起始行（包含，从0开始计数）
	 * @param endRowIndex   结束行（包含，从0开始计数）
	 */
	public AbstractSheetReader(final int startRowIndex, final int endRowIndex) {
		this(new CellRangeAddress(
			Math.min(startRowIndex, endRowIndex),
			Math.max(startRowIndex, endRowIndex),
			0, Integer.MAX_VALUE));
	}

	/**
	 * 构造
	 *
	 * @param cellRangeAddress 读取范围
	 */
	public AbstractSheetReader(final CellRangeAddress cellRangeAddress) {
		this.cellRangeAddress = cellRangeAddress;
	}

	/**
	 * 设置Excel配置
	 *
	 * @param config Excel配置
	 */
	public void setExcelConfig(final ExcelReadConfig config) {
		this.config = config;
	}
}
