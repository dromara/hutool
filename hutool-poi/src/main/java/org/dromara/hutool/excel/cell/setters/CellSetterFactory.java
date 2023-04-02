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
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * {@link CellSetter} 简单静态工厂类，用于根据值类型创建对应的{@link CellSetter}
 *
 * @author looly
 * @since 5.7.8
 */
public class CellSetterFactory {

	/**
	 * 创建值对应类型的{@link CellSetter}
	 *
	 * @param value 值
	 * @return {@link CellSetter}
	 */
	public static CellSetter createCellSetter(final Object value) {
		if (null == value) {
			return NullCellSetter.INSTANCE;
		} else if (value instanceof CellSetter) {
			return (CellSetter) value;
		} else if (value instanceof Date) {
			return new DateCellSetter((Date) value);
		} else if (value instanceof TemporalAccessor) {
			return new TemporalAccessorCellSetter((TemporalAccessor) value);
		} else if (value instanceof Calendar) {
			return new CalendarCellSetter((Calendar) value);
		} else if (value instanceof Boolean) {
			return new BooleanCellSetter((Boolean) value);
		} else if (value instanceof RichTextString) {
			return new RichTextCellSetter((RichTextString) value);
		} else if (value instanceof Number) {
			return new NumberCellSetter((Number) value);
		}else if (value instanceof Hyperlink) {
			return new HyperlinkCellSetter((Hyperlink) value);
		} else {
			return new CharSequenceCellSetter(value.toString());
		}
	}
}
