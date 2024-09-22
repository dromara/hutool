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
import org.dromara.hutool.core.convert.ConvertException;
import org.dromara.hutool.core.convert.MatcherConverter;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.date.SqlDateUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期转换器
 *
 * @author Looly
 */
public class DateConverter extends AbstractConverter implements MatcherConverter {
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
	 * @param format 日期格式，{@code null}表示无格式定义
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
	public boolean match(final Type targetType, final Class<?> rawType, final Object value) {
		return Date.class.isAssignableFrom(rawType);
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
			final Date date = StrUtil.isBlank(this.format) //
				? DateUtil.parse(valueStr) //
				: DateUtil.parse(valueStr, this.format);
			if (null != date) {
				return wrap(targetClass, date);
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
	private java.util.Date wrap(final Class<?> targetClass, final Date date) {
		if(targetClass == date.getClass()){
			return date;
		}

		return wrap(targetClass, date.getTime());
	}

	/**
	 * 时间戳转为子类型，支持：
	 * <ul>
	 *     <li>{@link java.util.Date}</li>
	 *     <li>{@link DateTime}</li>
	 *     <li>{@link java.sql.Date}</li>
	 *     <li>{@link java.sql.Time}</li>
	 *     <li>{@link java.sql.Timestamp}</li>
	 * </ul>
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

		final String dateClassName = targetClass.getName();
		if(dateClassName.startsWith("java.sql.")){
			// 为了解决在JDK9+模块化项目中用户没有引入java.sql模块导致的问题，此处增加判断
			// 如果targetClass是java.sql的类，说明引入了此模块
			return SqlDateUtil.wrap(targetClass, mills);
		}

		throw new ConvertException("Unsupported target Date type: {}", targetClass.getName());
	}
}
