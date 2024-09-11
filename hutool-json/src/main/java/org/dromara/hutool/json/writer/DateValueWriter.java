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

package org.dromara.hutool.json.writer;

import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.TemporalAccessorUtil;
import org.dromara.hutool.core.date.format.GlobalCustomFormat;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.json.InternalJSONUtil;

import java.time.MonthDay;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类型的值写出器<br>
 * 支持包括：{@link Date}、{@link Calendar}、{@link TemporalAccessor}
 *
 * @author looly
 * @since 6.0.0
 */
public class DateValueWriter implements JSONValueWriter {
	/**
	 * 单例对象
	 */
	public static final DateValueWriter INSTANCE = new DateValueWriter();

	@Override
	public boolean test(final Object value) {
		return value instanceof Date || value instanceof Calendar || value instanceof TemporalAccessor;
	}

	@Override
	public void write(final JSONWriter writer, final Object value) {
		final String rawString;
		// issue#2572@Github
		if (value instanceof MonthDay) {
			rawString = InternalJSONUtil.quote(value.toString());
		}else{
			rawString = formatDate(value, writer.getConfig().getDateFormat());
		}
		writer.writeRaw(rawString);
	}

	/**
	 * 按照给定格式格式化日期，格式为空时返回时间戳字符串<br>
	 * 如果给定的格式是时间戳，直接返回时间戳字符串，如果是给定字符串格式，返回带双引号包装的字符串
	 *
	 * @param dateObj Date或者Calendar对象
	 * @param format  格式
	 * @return 日期字符串
	 */
	private static String formatDate(final Object dateObj, final String format) {
		if (StrUtil.isNotBlank(format)) {
			final String dateStr;
			if (dateObj instanceof TemporalAccessor) {
				dateStr = TemporalAccessorUtil.format((TemporalAccessor) dateObj, format);
			} else {
				dateStr = DateUtil.format(ConvertUtil.toDate(dateObj), format);
			}

			if (GlobalCustomFormat.FORMAT_SECONDS.equals(format)
				|| GlobalCustomFormat.FORMAT_MILLISECONDS.equals(format)) {
				// Hutool自定义的秒和毫秒表示，默认不包装双引号
				return dateStr;
			}
			//用户定义了日期格式
			return InternalJSONUtil.quote(dateStr);
		}

		//默认使用时间戳
		final long timeMillis;
		if (dateObj instanceof TemporalAccessor) {
			timeMillis = TemporalAccessorUtil.toEpochMilli((TemporalAccessor) dateObj);
		} else if (dateObj instanceof Date) {
			timeMillis = ((Date) dateObj).getTime();
		} else if (dateObj instanceof Calendar) {
			timeMillis = ((Calendar) dateObj).getTimeInMillis();
		} else {
			throw new UnsupportedOperationException("Unsupported Date type: " + dateObj.getClass());
		}
		return String.valueOf(timeMillis);
	}
}
