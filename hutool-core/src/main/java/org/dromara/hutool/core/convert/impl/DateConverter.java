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

package org.dromara.hutool.core.convert.impl;

import org.dromara.hutool.core.convert.AbstractConverter;
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.SqlDateUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;

/**
 * 日期转换器
 *
 * @author Looly
 */
public class DateConverter extends AbstractConverter {
	private static final long serialVersionUID = 1L;

	/**
	 * 单例
	 */
	public static final DateConverter INSTANCE = new DateConverter();

	/**
	 * 日期格式化
	 */
	private String format;

	/**
	 * 构造
	 */
	public DateConverter() {
		this(null);
	}

	/**
	 * 构造
	 *
	 * @param format 日期格式
	 */
	public DateConverter(final String format) {
		this.format = format;
	}

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
	protected java.util.Date convertInternal(final Class<?> targetClass, final Object value) {
		if (value == null || (value instanceof CharSequence && StrUtil.isBlank(value.toString()))) {
			return null;
		}
		if (value instanceof TemporalAccessor) {
			return wrap(targetClass, DateUtil.date((TemporalAccessor) value));
		} else if (value instanceof Calendar) {
			return wrap(targetClass, DateUtil.date((Calendar) value));
		} else if (null == this.format && value instanceof Number) {
			return wrap(targetClass, ((Number) value).longValue());
		} else {
			// 统一按照字符串处理
			final String valueStr = convertToStr(value);
			final DateTime dateTime = StrUtil.isBlank(this.format) //
					? DateUtil.parse(valueStr) //
					: DateUtil.parse(valueStr, this.format);
			if (null != dateTime) {
				return wrap(targetClass, dateTime);
			}
		}

		throw new ConvertException("Can not convert {}:[{}] to {}", value.getClass().getName(), value, targetClass.getName());
	}

	/**
	 * java.util.Date转为子类型
	 *
	 * @param date Date
	 * @return 目标类型对象
	 */
	private java.util.Date wrap(final Class<?> targetClass, final DateTime date) {
		// 返回指定类型
		if (java.util.Date.class == targetClass) {
			return date.toJdkDate();
		}
		if (DateTime.class == targetClass) {
			return date;
		}
		if (java.sql.Date.class == targetClass) {
			return SqlDateUtil.date(date);
		}
		if (java.sql.Time.class == targetClass) {
			return SqlDateUtil.time(date);
		}
		if (java.sql.Timestamp.class == targetClass) {
			return SqlDateUtil.timestamp(date);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupported target Date type: {}", targetClass.getName()));
	}

	/**
	 * java.util.Date转为子类型
	 *
	 * @param mills Date
	 * @return 目标类型对象
	 */
	private java.util.Date wrap(final Class<?> targetClass, final long mills) {
		// 返回指定类型
		if (java.util.Date.class == targetClass) {
			return new java.util.Date(mills);
		}
		if (DateTime.class == targetClass) {
			return DateUtil.date(mills);
		}
		if (java.sql.Date.class == targetClass) {
			return new java.sql.Date(mills);
		}
		if (java.sql.Time.class == targetClass) {
			return new java.sql.Time(mills);
		}
		if (java.sql.Timestamp.class == targetClass) {
			return new java.sql.Timestamp(mills);
		}

		throw new UnsupportedOperationException(StrUtil.format("Unsupported target Date type: {}", targetClass.getName()));
	}
}
