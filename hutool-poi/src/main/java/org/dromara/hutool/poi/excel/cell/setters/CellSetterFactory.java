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

import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;

import java.io.File;
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
		}else if (value instanceof byte[]) {
			// 二进制理解为图片
			return new PicCellSetter((byte[]) value);
		}else if (value instanceof File) {
			return new PicCellSetter((File) value);
		} else {
			return new CharSequenceCellSetter(value.toString());
		}
	}
}
