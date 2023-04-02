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

package org.dromara.hutool.date.format;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 抽象日期基本信息类，包括日期格式、时区、本地化等信息
 *
 * @author looly
 */
public class SimpleDateBasic implements DateBasic, Serializable {
	private static final long serialVersionUID = 6333136319870641818L;

	/**
	 * The pattern
	 */
	protected final String pattern;
	/**
	 * The time zone.
	 */
	protected final TimeZone timeZone;
	/**
	 * The locale.
	 */
	protected final Locale locale;

	/**
	 * 构造，内部使用
	 *
	 * @param pattern  使用{@link java.text.SimpleDateFormat} 相同的日期格式
	 * @param timeZone 非空时区{@link TimeZone}
	 * @param locale   非空{@link Locale} 日期地理位置
	 */
	protected SimpleDateBasic(final String pattern, final TimeZone timeZone, final Locale locale) {
		this.pattern = pattern;
		this.timeZone = timeZone;
		this.locale = locale;
	}

	// ----------------------------------------------------------------------- Accessors
	@Override
	public String getPattern() {
		return pattern;
	}

	@Override
	public TimeZone getTimeZone() {
		return timeZone;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	// ----------------------------------------------------------------------- Basics
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof FastDatePrinter == false) {
			return false;
		}
		final SimpleDateBasic other = (SimpleDateBasic) obj;
		return pattern.equals(other.pattern) && timeZone.equals(other.timeZone) && locale.equals(other.locale);
	}

	@Override
	public int hashCode() {
		return pattern.hashCode() + 13 * (timeZone.hashCode() + 13 * locale.hashCode());
	}

	@Override
	public String toString() {
		return "FastDatePrinter[" + pattern + "," + locale + "," + timeZone.getID() + "]";
	}
}
