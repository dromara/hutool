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

package org.dromara.hutool.poi.excel.cell.setters;

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
