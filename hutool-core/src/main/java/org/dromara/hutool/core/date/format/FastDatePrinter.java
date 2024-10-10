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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link java.text.SimpleDateFormat} 的线程安全版本，用于将 {@link Date} 格式化输出<br>
 * Thanks to Apache Commons Lang 3.5
 *
 * @see FastDateParser
 */
public class FastDatePrinter extends SimpleDateBasic implements DatePrinter {
	private static final long serialVersionUID = -6305750172255764887L;

	private final DatePattern datePattern;

	// Constructor
	// -----------------------------------------------------------------------
	/**
	 * 构造，内部使用<br>
	 *
	 * @param pattern 使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区{@link TimeZone}
	 * @param locale 非空{@link Locale} 日期地理位置
	 */
	public FastDatePrinter(final String pattern, final TimeZone timeZone, final Locale locale) {
		super(pattern, timeZone, locale);
		this.datePattern = new DatePattern(pattern, locale, timeZone);
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
	public String format(final long millis) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTimeInMillis(millis);
		return applyRulesToString(c);
	}

	@Override
	public String format(final Date date) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTime(date);
		return applyRulesToString(c);
	}

	@Override
	public String format(final Calendar calendar) {
		return format(calendar, new StringBuilder(datePattern.getEstimateLength())).toString();
	}

	@Override
	public <B extends Appendable> B format(final Date date, final B buf) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTime(date);
		return datePattern.applyRules(c, buf);
	}

	@Override
	public <B extends Appendable> B format(final long millis, final B buf) {
		final Calendar c = Calendar.getInstance(timeZone, locale);
		c.setTimeInMillis(millis);
		return datePattern.applyRules(c, buf);
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
	 * Creates a String representation of the given Calendar by applying the rules of this printer to it.
	 *
	 * @param c the Calender to apply the rules to.
	 * @return a String representation of the given Calendar.
	 */
	private String applyRulesToString(final Calendar c) {
		return datePattern.applyRules(c, new StringBuilder(datePattern.getEstimateLength())).toString();
	}

	/**
	 *估算生成的日期字符串长度<br>
	 * 实际生成的字符串长度小于或等于此值
	 *
	 * @return 日期字符串长度
	 */
	public int getMaxLengthEstimate() {
		return datePattern.getEstimateLength();
	}
}
