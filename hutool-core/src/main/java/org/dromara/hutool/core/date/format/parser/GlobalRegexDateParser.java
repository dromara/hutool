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

import org.dromara.hutool.core.date.DateTime;

import java.util.regex.Pattern;

/**
 * 全局正则日期解析器<br>
 * 通过使用预定义或自定义的正则规则，解析日期字符串
 *
 * @author Looly
 * @since 6.0.0
 */
public class GlobalRegexDateParser {

	private static final RegexDateParser PARSER;

	static {
		final String yearRegex = "(?<year>\\d{2,4})";
		// 月的正则，匹配：Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec，
		// 或 January, February, March, April, May, June, July, August, September, October, November, December
		final String monthRegex = "(?<month>[jfmaasond][aepucoe][nbrylgptvc]\\w{0,6})";
		final String dayRegex = "(?<day>\\d{1,2})(?:th)?";
		// 周的正则，匹配：Mon, Tue, Wed, Thu, Fri, Sat, Sun，
		// 或 Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
		// 周一般出现在日期头部，可选
		final String weekRegexWithSuff = "(?<week>[mwfts][oeruha][ndieut](\\w{3,6})?\\W+)?";
		// hh:mm:ss.SSSSZ hh:mm:ss.SSSS hh:mm:ss hh:mm
		final String timeRegexWithPre = "(" +
			"(\\W+|T)(at\\s)?(?<hour>\\d{1,2})" +
			"\\W(?<minute>\\d{1,2})" +
			"(\\W(?<second>\\d{1,2}))?秒?" +
			"(?:[.,](?<ns>\\d{1,9}))?(?<zero>z)?" +
			"(\\s?(?<m>[ap]m))?" +
			")?";
		// 月开头，类似：May 8
		final String dateRegexMonthFirst = monthRegex + "\\W+" + dayRegex;
		// 日开头，类似：02-Jan
		final String dateRegexDayFirst = dayRegex + "\\W+" + monthRegex;
		// 时区拼接，类似：
		// GMT+0100 (GMT Daylight Time)
		// +0200 (CEST)
		// GMT+0100
		// MST
		final String zoneRegex = "\\s?(?<zone>"
			// 匹配：GMT MST等
			+ "[a-z ]*"
			// 匹配：+08:00 +0800 +08
			+ "(\\s?[-+]\\d{1,2}:?(?:\\d{2})?)*"
			// 匹配：(GMT Daylight Time)等
			+ "(\\s?[(]?[a-z ]+[)]?)?"
			+ ")";
		final String maskRegex = "(\\smsk m=[+-]\\d[.]\\d+)?";

		PARSER = RegexDateParser.of(
			//【年月日时】类似：2009-Feb-08，时间部分可选，类似：5:57:51，05:57:51 +08:00
			yearRegex + "\\W" + dateRegexMonthFirst + timeRegexWithPre + zoneRegex + maskRegex,
			//【年月日时】类似：2009-02-08或2014年04月08日，时间部分可选，类似：5:57:51，05:57:51 +08:00
			yearRegex + "\\W(?<month>\\d{1,2})(\\W(?<day>\\d{1,2}))?日?" + timeRegexWithPre + zoneRegex + maskRegex,

			//【周月日年时】类似：May 8, 2009，时间部分可选，类似：5:57:51，05:57:51 +08:00
			weekRegexWithSuff + dateRegexMonthFirst + "\\W+" + yearRegex + timeRegexWithPre + zoneRegex + maskRegex,
			//【周月日时年】类似：Mon Jan 2 15:04:05 MST 2006
			weekRegexWithSuff + dateRegexMonthFirst + timeRegexWithPre + zoneRegex + "\\W+" + yearRegex + maskRegex,
			//【周日月年时】类似：Monday, 02-Jan-06 15:04:05 MST
			weekRegexWithSuff + dateRegexDayFirst + "\\W+" + yearRegex + timeRegexWithPre + zoneRegex + maskRegex,
			//【日月年时】MM/dd/yyyy, dd/MM/yyyy, 类似：4/12/2014 03:00:51，为避免歧义，只支持4位年
			"(?<dayOrMonth>\\d{1,2}\\W\\d{1,2})\\W(?<year>\\d{4})" + timeRegexWithPre + zoneRegex + maskRegex,

			//纯数字日期时间
			// yyyy
			// yyyyMM
			// yyyyMMdd
			// yyyyMMddhhmmss
			// unixtime(10)
			// millisecond(13)
			// microsecond(16)
			// nanosecond(19)
			"^(?<number>\\d{4,19})$"
		);
	}

	/**
	 * 解析日期，此方法线程安全
	 *
	 * @param source 日期字符串
	 * @return 日期
	 */
	public static DateTime parse(final CharSequence source) {
		return (DateTime) PARSER.parse(source);
	}

	/**
	 * 当用户传入的月和日无法判定默认位置时，设置默认的日期格式为dd/mm还是mm/dd
	 *
	 * @param preferMonthFirst {@code true}默认为mm/dd，否则dd/mm
	 */
	synchronized public static void setPreferMonthFirst(final boolean preferMonthFirst) {
		PARSER.setPreferMonthFirst(preferMonthFirst);
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
