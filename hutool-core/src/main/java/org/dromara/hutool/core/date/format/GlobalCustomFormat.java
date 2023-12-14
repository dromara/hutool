/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date.format;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.SafeConcurrentHashMap;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * 全局自定义格式<br>
 * 用于定义用户指定的日期格式和输出日期的关系
 *
 * @author looly
 * @since 5.7.2
 */
public class GlobalCustomFormat {

	/**
	 * 格式：秒时间戳（Unix时间戳）
	 */
	public static final String FORMAT_SECONDS = "#sss";
	/**
	 * 格式：毫秒时间戳
	 */
	public static final String FORMAT_MILLISECONDS = "#SSS";

	private static final Map<CharSequence, Function<Date, String>> formatterMap;
	private static final Map<CharSequence, Function<CharSequence, Date>> parserMap;

	static {
		formatterMap = new SafeConcurrentHashMap<>();
		parserMap = new SafeConcurrentHashMap<>();

		// Hutool预设的几种自定义格式
		putFormatter(FORMAT_SECONDS, (date) -> String.valueOf(Math.floorDiv(date.getTime(), 1000L)));
		putParser(FORMAT_SECONDS, (dateStr) -> DateUtil.date(Math.multiplyExact(Long.parseLong(dateStr.toString()), 1000L)));

		putFormatter(FORMAT_MILLISECONDS, (date) -> String.valueOf(date.getTime()));
		putParser(FORMAT_MILLISECONDS, (dateStr) -> DateUtil.date(Long.parseLong(dateStr.toString())));
	}

	/**
	 * 加入日期格式化规则
	 *
	 * @param format 格式
	 * @param func   格式化函数
	 */
	public static void putFormatter(final String format, final Function<Date, String> func) {
		Assert.notNull(format, "Format must be not null !");
		Assert.notNull(func, "Function must be not null !");
		formatterMap.put(format, func);
	}

	/**
	 * 加入日期解析规则
	 *
	 * @param format 格式
	 * @param func   解析函数
	 */
	public static void putParser(final String format, final Function<CharSequence, Date> func) {
		Assert.notNull(format, "Format must be not null !");
		Assert.notNull(func, "Function must be not null !");
		parserMap.put(format, func);
	}

	/**
	 * 检查指定格式是否为自定义格式
	 *
	 * @param format 格式
	 * @return 是否为自定义格式
	 */
	public static boolean isCustomFormat(final String format) {
		return formatterMap.containsKey(format);
	}

	/**
	 * 使用自定义格式格式化日期
	 *
	 * @param date   日期
	 * @param format 自定义格式
	 * @return 格式化后的日期
	 */
	public static String format(final Date date, final CharSequence format) {
		if (null != formatterMap) {
			final Function<Date, String> func = formatterMap.get(format);
			if (null != func) {
				return func.apply(date);
			}
		}

		return null;
	}

	/**
	 * 使用自定义格式格式化日期
	 *
	 * @param temporalAccessor 日期
	 * @param format           自定义格式
	 * @return 格式化后的日期
	 */
	public static String format(final TemporalAccessor temporalAccessor, final CharSequence format) {
		return format(DateUtil.date(temporalAccessor), format);
	}

	/**
	 * 使用自定义格式解析日期
	 *
	 * @param dateStr 日期字符串
	 * @param format  自定义格式
	 * @return 格式化后的日期
	 */
	public static Date parse(final CharSequence dateStr, final String format) {
		if (null != parserMap) {
			final Function<CharSequence, Date> func = parserMap.get(format);
			if (null != func) {
				return func.apply(dateStr);
			}
		}

		return null;
	}
}
