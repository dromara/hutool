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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.date.CalendarUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.text.StrUtil;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换器
 *
 * @author Looly
 *
 */
public class CalendarConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/** 日期格式化 */
	private String format;

	/**
	 * 获取日期格式
	 *
	 * @return 设置日期格式
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * 设置日期格式
	 *
	 * @param format 日期格式
	 */
	public void setFormat(final String format) {
		this.format = format;
	}

	@Override
	protected Calendar convertInternal(final Class<?> targetClass, final Object value) {
		// Handle Date
		if (value instanceof Date) {
			return CalendarUtil.calendar((Date)value);
		}

		// Handle Long
		if (value instanceof Long) {
			//此处使用自动拆装箱
			return CalendarUtil.calendar((Long)value);
		}

		if(value instanceof XMLGregorianCalendar){
			return CalendarUtil.calendar((XMLGregorianCalendar) value);
		}

		final String valueStr = convertToStr(value);
		return CalendarUtil.calendar(StrUtil.isBlank(format) ? DateUtil.parse(valueStr) : DateUtil.parse(valueStr, format));
	}

}
