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

package org.dromara.hutool.poi.excel.cell.editors;

import org.apache.poi.ss.usermodel.Cell;

import org.dromara.hutool.poi.excel.cell.CellEditor;

/**
 * POI中NUMRIC类型的值默认返回的是Double类型，此编辑器用于转换其为int型
 *
 * @author Looly
 */
public class NumericToIntEditor implements CellEditor {

	@Override
	public Object edit(final Cell cell, final Object value) {
		if (value instanceof Number) {
			return ((Number) value).intValue();
		}
		return value;
	}

}
