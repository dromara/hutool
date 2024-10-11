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

package org.dromara.hutool.core.date.format;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.map.concurrent.SafeConcurrentHashMap;

import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * 自定义格式化管理器，用于自定义日期格式化和解析逻辑<br>
 * 一般通过{@link #getInstance()}使用全局单例对象。<br>
 * 通过{@link #registerFormatter(String, Function)}注册自定义格式化规则，注册后使用{@link #format(Date, CharSequence)}格式化为日期字符串<br>
 * 通过{@link #registerParser(String, Function)}注册自定义解析规则，注册后使用{@link #parse(CharSequence, String)}解析日期字符串<br>
 *
 * @author looly
 */
public class DateFormatManager {

	/**
	 * 格式：秒时间戳（Unix时间戳）
	 */
	public static final String FORMAT_SECONDS = "#sss";
	/**
	 * 格式：毫秒时间戳
	 */
	public static final String FORMAT_MILLISECONDS = "#SSS";

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static final DateFormatManager INSTANCE = new DateFormatManager();
	}

	/**
	 * 获得单例的 DateFormatManager
	 *
	 * @return DateFormatManager
	 */
	public static DateFormatManager getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private final Map<CharSequence, Function<Date, String>> formatterMap;
	private final Map<CharSequence, Function<CharSequence, Date>> parserMap;

	/**
	 * 构造
	 */
	public DateFormatManager() {
		formatterMap = new SafeConcurrentHashMap<>();
		parserMap = new SafeConcurrentHashMap<>();

		// Hutool预设的几种自定义格式
		registerFormatter(FORMAT_SECONDS, (date) -> String.valueOf(Math.floorDiv(date.getTime(), 1000L)));
		registerParser(FORMAT_SECONDS, (dateStr) -> DateUtil.date(Math.multiplyExact(Long.parseLong(dateStr.toString()), 1000L)));

		registerFormatter(FORMAT_MILLISECONDS, (date) -> String.valueOf(date.getTime()));
		registerParser(FORMAT_MILLISECONDS, (dateStr) -> DateUtil.date(Long.parseLong(dateStr.toString())));
	}

	// region ----- register
	/**
	 * 加入日期格式化规则
	 *
	 * @param format 格式
	 * @param func   格式化函数
	 * @return this
	 */
	public DateFormatManager registerFormatter(final String format, final Function<Date, String> func) {
		Assert.notNull(format, "Format must be not null !");
		Assert.notNull(func, "Function must be not null !");
		formatterMap.put(format, func);
		return this;
	}

	/**
	 * 加入日期解析规则
	 *
	 * @param format 格式
	 * @param func   解析函数
	 * @return this
	 */
	public DateFormatManager registerParser(final String format, final Function<CharSequence, Date> func) {
		Assert.notNull(format, "Format must be not null !");
		Assert.notNull(func, "Function must be not null !");
		parserMap.put(format, func);
		return this;
	}
	// endregion

	// region ----- format
	/**
	 * 检查指定格式是否为自定义格式
	 *
	 * @param format 格式
	 * @return 是否为自定义格式
	 */
	public boolean isCustomFormat(final String format) {
		return null != formatterMap && formatterMap.containsKey(format);
	}

	/**
	 * 使用自定义格式格式化日期
	 *
	 * @param temporalAccessor 日期
	 * @param format           自定义格式
	 * @return 格式化后的日期
	 */
	public String format(final TemporalAccessor temporalAccessor, final CharSequence format) {
		return format(DateUtil.date(temporalAccessor), format);
	}

	/**
	 * 使用自定义格式格式化日期
	 *
	 * @param date   日期
	 * @param format 自定义格式
	 * @return 格式化后的日期
	 */
	public String format(final Date date, final CharSequence format) {
		if (null != formatterMap) {
			final Function<Date, String> func = formatterMap.get(format);
			if (null != func) {
				return func.apply(date);
			}
		}

		return null;
	}
	// endregion

	// region ----- parse
	/**
	 * 检查指定格式是否为自定义格式
	 *
	 * @param format 格式
	 * @return 是否为自定义格式
	 */
	public boolean isCustomParse(final String format) {
		return null != parserMap && parserMap.containsKey(format);
	}

	/**
	 * 使用自定义格式解析日期
	 *
	 * @param dateStr 日期字符串
	 * @param format  自定义格式
	 * @return 格式化后的日期
	 */
	public Date parse(final CharSequence dateStr, final String format) {
		if (null != parserMap) {
			final Function<CharSequence, Date> func = parserMap.get(format);
			if (null != func) {
				return func.apply(dateStr);
			}
		}
		return null;
	}
	// endregion
}
