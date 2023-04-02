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

package org.dromara.hutool.excel.cell.setters;

import org.dromara.hutool.excel.cell.CellSetter;
import org.apache.poi.ss.usermodel.Cell;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * {@link TemporalAccessor} 值单元格设置器
 *
 * @author looly
 * @since 5.7.8
 */
public class TemporalAccessorCellSetter implements CellSetter {

	private final TemporalAccessor value;

	/**
	 * 构造
	 *
	 * @param value 值
	 */
	TemporalAccessorCellSetter(final TemporalAccessor value) {
		this.value = value;
	}

	@Override
	public void setValue(final Cell cell) {
		if (value instanceof Instant) {
			cell.setCellValue(Date.from((Instant) value));
		} else if (value instanceof LocalDateTime) {
			cell.setCellValue((LocalDateTime) value);
		} else if (value instanceof LocalDate) {
			cell.setCellValue((LocalDate) value);
		}
	}
}
