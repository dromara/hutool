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

package org.dromara.hutool.serialize;

import org.dromara.hutool.JSONConfig;
import org.dromara.hutool.convert.Convert;
import org.dromara.hutool.date.DateUtil;
import org.dromara.hutool.date.TemporalAccessorUtil;
import org.dromara.hutool.date.format.GlobalCustomFormat;
import org.dromara.hutool.text.StrUtil;
import org.dromara.hutool.InternalJSONUtil;

import java.time.MonthDay;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 自定义的日期表示形式<br>
 * 支持包括：{@link Date}、{@link Calendar}、{@link TemporalAccessor}
 *
 * @author looly
 * @since 6.0.0
 */
public class DateJSONString implements JSONStringer {

	final Object dateObj;
	final JSONConfig jsonConfig;

	/**
	 * 构造
	 *
	 * @param dateObj    日期对象，支持包括Date、Calendar、TemporalAccessor
	 * @param jsonConfig JSON配置
	 */
	public DateJSONString(final Object dateObj, final JSONConfig jsonConfig) {
		this.dateObj = dateObj;
		this.jsonConfig = jsonConfig;
	}

	/**
	 * 获取原始的日期对象{@link Date}、{@link Calendar}、{@link TemporalAccessor}
	 *
	 * @return 日期对象
	 */
	@Override
	public Object getRaw() {
		return this.dateObj;
	}

	@Override
	public String toJSONString() {
		// issue#2572@Github
		if (dateObj instanceof MonthDay) {
			return InternalJSONUtil.quote(dateObj.toString());
		}

		return formatDate(this.dateObj, this.jsonConfig.getDateFormat());
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
				dateStr = DateUtil.format(Convert.toDate(dateObj), format);
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
