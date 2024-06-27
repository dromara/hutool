/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date.format.parser;

import java.util.Date;
import java.util.regex.Pattern;

/**
 * 全局正则日期解析器<br>
 * 通过使用预定义或自定义的正则规则，解析日期字符串
 *
 * @author Looly
 * @since 6.0.0
 */
public class GlobalRegexDateParser {

	// hh:mm:ss.SSSSZ hh:mm:ss.SSSS hh:mm:ss hh:mm
	private static final String timeRegex = "(" +
		"\\s(?<hour>\\d{1,2})" +
		":(?<minute>\\d{1,2})" +
		"(:(?<second>\\d{1,2}))?" +
		"(?:[.,](?<ns>\\d{1,9}))?(?<zero>z)?" +
		"(\\s?(?<m>am|pm))?" +
		")?";
	// +08:00 +0800 +08
	private static final String zoneOffsetRegex = "(\\s?(?<zoneOffset>[-+]\\d{1,2}:?(?:\\d{2})?))?";
	// CST UTC (CST)
	private static final String zoneNameRegex = "(\\s[(]?(?<zoneName>[a-z ]+)[)]?)?";

	private static final RegexDateParser PARSER;

	static {
		final String dateRegexMonthFirst = "(?<month>\\w+{3,9})\\W+(?<day>\\d{1,2})(?:th)?\\W+(?<year>\\d{2,4})";

		PARSER = RegexDateParser.of(
			// 年开头

			//月开头，类似：May 8, 2009 5:57:51
			dateRegexMonthFirst + timeRegex + zoneOffsetRegex

			// 周开头

			// 日开头
		);
	}

	/**
	 * 解析日期，此方法线程安全
	 *
	 * @param source 日期字符串
	 * @return 日期
	 */
	public static Date parse(final CharSequence source) {
		return PARSER.parse(source);
	}

	/**
	 * 新增自定义日期正则
	 *
	 * @param regex 日期正则
	 */
	synchronized public static void registerRegex(final String regex) {
		PARSER.addRegex(regex);
	}

	/**
	 * 新增自定义日期正则
	 *
	 * @param pattern 日期正则
	 */
	synchronized public static void registerPattern(final Pattern pattern) {
		PARSER.addPattern(pattern);
	}
}
