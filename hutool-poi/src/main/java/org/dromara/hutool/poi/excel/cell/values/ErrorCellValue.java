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

package org.dromara.hutool.poi.excel.cell.values;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.poi.excel.cell.CellValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaError;

/**
 * ERROR类型单元格值
 *
 * @author looly
 * @since 5.7.8
 */
public class ErrorCellValue implements CellValue<String> {

	private final Cell cell;

	/**
	 * 构造
	 *
	 * @param cell {@link Cell}
	 */
	public ErrorCellValue(final Cell cell){
		this.cell = cell;
	}

	@Override
	public String getValue() {
		final FormulaError error = FormulaError.forInt(cell.getErrorCellValue());
		return (null == error) ? StrUtil.EMPTY : error.getString();
	}
}
