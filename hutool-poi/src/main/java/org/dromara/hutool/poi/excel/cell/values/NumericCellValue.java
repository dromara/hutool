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

import org.dromara.hutool.core.util.CharUtil;
import org.dromara.hutool.poi.excel.ExcelDateUtil;
import org.dromara.hutool.poi.excel.cell.CellValue;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.NumberToTextConverter;

import java.time.LocalDateTime;

/**
 * 数字类型单元格值<br>
 * 单元格值可能为Long、Double、Date
 *
 * @author looly
 * @since 5.7.8
 */
public class NumericCellValue implements CellValue<Object> {

	private final Cell cell;

	/**
	 * 构造
	 *
	 * @param cell {@link Cell}
	 */
	public NumericCellValue(final Cell cell) {
		this.cell = cell;
	}

	@Override
	public Object getValue() {
		final double value = cell.getNumericCellValue();

		final CellStyle style = cell.getCellStyle();
		if (null != style) {
			// 判断是否为日期
			if (ExcelDateUtil.isDateFormat(cell)) {
				final LocalDateTime date = cell.getLocalDateTimeCellValue();
				// 1899年写入会导致数据错乱，读取到1899年证明这个单元格的信息不关注年月日
				if(1899 == date.getYear()){
					return date.toLocalTime();
				}
				return date;
			}

			final String format = style.getDataFormatString();
			// 普通数字
			if (null != format && format.indexOf(CharUtil.DOT) < 0) {
				final long longPart = (long) value;
				if (((double) longPart) == value) {
					// 对于无小数部分的数字类型，转为Long
					return longPart;
				}
			}
		}

		// 某些Excel单元格值为double计算结果，可能导致精度问题，通过转换解决精度问题。
		return Double.parseDouble(NumberToTextConverter.toText(value));
	}
}
