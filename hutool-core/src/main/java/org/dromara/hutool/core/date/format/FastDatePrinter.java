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

import org.dromara.hutool.core.date.format.parser.FastDateParser;
import org.dromara.hutool.core.text.StrUtil;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * {@link java.text.SimpleDateFormat} 的线程安全版本，用于将 {@link Date} 格式化输出<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @see FastDateParser
 */
public class FastDatePrinter extends SimpleDateBasic implements DatePrinter {
	private static final long serialVersionUID = -6305750172255764887L;

	private final DatePattern datePattern;
	/**
	 * 缓存的Calendar对象，用于减少对象创建。参考tomcat的ConcurrentDateFormat
	 */
	private final Queue<Calendar> queue;

	// Constructor
	// -----------------------------------------------------------------------

	/**
	 * 构造，内部使用<br>
	 *
	 * @param pattern  使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区{@link TimeZone}
	 * @param locale   非空{@link Locale} 日期地理位置
	 */
	public FastDatePrinter(final String pattern, final TimeZone timeZone, final Locale locale) {
		super(pattern, timeZone, locale);
		this.datePattern = new DatePattern(pattern, locale, timeZone);
		this.queue = new ConcurrentLinkedQueue<>();
	}

	// Format methods
	// -----------------------------------------------------------------------

	/**
	 * <p>
	 * Formats a {@code Date}, {@code Calendar} or {@code Long} (milliseconds) object.
	 * </p>
	 *
	 * @param obj the object to format
	 * @return The formatted value.
	 */
	String format(final Object obj) {
		if (obj instanceof Date) {
			return format((Date) obj);
		} else if (obj instanceof Calendar) {
			return format((Calendar) obj);
		} else if (obj instanceof Long) {
			return format(((Long) obj).longValue());
		} else {
			throw new IllegalArgumentException("Unknown class: " + (obj == null ? "<null>" : obj.getClass().getName()));
		}
	}

	@Override
	public String format(final Date date) {
		return format(date.getTime());
	}

	@Override
	public String format(final long millis) {
		return format(millis, StrUtil.builder(datePattern.getEstimateLength())).toString();
	}

	@Override
	public String format(final Calendar calendar) {
		return format(calendar, StrUtil.builder(datePattern.getEstimateLength())).toString();
	}

	@Override
	public <B extends Appendable> B format(final Date date, final B buf) {
		return format(date.getTime(), buf);
	}

	@Override
	public <B extends Appendable> B format(final long millis, final B buf) {
		return applyRules(millis, buf);
	}

	@Override
	public <B extends Appendable> B format(Calendar calendar, final B buf) {
		// do not pass in calendar directly, this will cause TimeZone of FastDatePrinter to be ignored
		if (!calendar.getTimeZone().equals(timeZone)) {
			calendar = (Calendar) calendar.clone();
			calendar.setTimeZone(timeZone);
		}
		return datePattern.applyRules(calendar, buf);
	}

	/**
	 * 根据规则将时间戳转换为Appendable，复用Calendar对象，避免创建新对象
	 *
	 * @param millis 时间戳
	 * @param buf    待拼接的 Appendable
	 * @return buf 拼接后的Appendable
	 */
	private <B extends Appendable> B applyRules(final long millis, final B buf) {
		Calendar calendar = queue.poll();
		if (calendar == null) {
			calendar = Calendar.getInstance(timeZone, locale);
		}
		calendar.setTimeInMillis(millis);
		final B b = datePattern.applyRules(calendar, buf);
		queue.offer(calendar);
		return b;
	}

	/**
	 * 估算生成的日期字符串长度<br>
	 * 实际生成的字符串长度小于或等于此值
	 *
	 * @return 日期字符串长度
	 */
	public int getMaxLengthEstimate() {
		return datePattern.getEstimateLength();
	}
}
