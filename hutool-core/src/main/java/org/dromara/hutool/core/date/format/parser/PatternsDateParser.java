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

package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.CalendarUtil;
import org.dromara.hutool.core.date.DateException;
import org.dromara.hutool.core.date.DateTime;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

/**
 * 通过给定的日期格式解析日期时间字符串。<br>
 * 传入的日期格式会逐个尝试，直到解析成功，返回{@link Calendar}对象，否则抛出{@link DateException}异常。
 *
 * @author looly
 * @since 6.0.0
 */
public class PatternsDateParser implements DateParser, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 创建 PatternsDateParser
	 *
	 * @param parsePatterns 多个日期格式
	 * @return PatternsDateParser
	 */
	public static PatternsDateParser of(final String... parsePatterns) {
		return new PatternsDateParser(parsePatterns);
	}

	private String[] parsePatterns;
	private Locale locale;

	/**
	 * 构造
	 *
	 * @param parsePatterns 多个日期格式
	 */
	public PatternsDateParser(final String... parsePatterns) {
		this.parsePatterns = parsePatterns;
	}

	/**
	 * 设置多个日期格式
	 *
	 * @param parsePatterns 日期格式列表
	 * @return this
	 */
	public PatternsDateParser setParsePatterns(final String... parsePatterns) {
		this.parsePatterns = parsePatterns;
		return this;
	}

	/**
	 * 设置{@link Locale}
	 *
	 * @param locale {@link Locale}
	 * @return this
	 */
	public PatternsDateParser setLocale(final Locale locale) {
		this.locale = locale;
		return this;
	}

	/**
	 * 获取{@link Locale}
	 *
	 * @return {@link Locale}
	 */
	public Locale getLocale() {
		return locale;
	}

	@Override
	public DateTime parse(final CharSequence source) {
		return new DateTime(CalendarUtil.parseByPatterns(source, this.locale, this.parsePatterns));
	}
}
