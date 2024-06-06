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

import org.dromara.hutool.core.date.*;
import org.dromara.hutool.core.date.format.DefaultDateBasic;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则日期解析器<br>
 * 通过定义一个命名分组的正则匹配日期格式，使用正则分组获取日期各部分的值，命名分组使用{@code  (?<xxx>子表达式) }表示，如：<br>
 * <pre>{@code
 *  ^(?<year>\d{4})(?<month>\d{2})$ 匹配 201909
 * }</pre>
 *
 * @author Looly
 * @since 6.0.0
 */
public class RegexDateParser extends DefaultDateBasic implements PredicateDateParser {
	private static final long serialVersionUID = 1L;

	private static final int[] NSS = {100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1};

	/**
	 * 根据给定带名称的分组正则，创建RegexDateParser
	 *
	 * @param regex 正则表达式
	 * @return RegexDateParser
	 */
	public static RegexDateParser of(final String regex) {
		return new RegexDateParser(Pattern.compile(regex));
	}

	private final Pattern pattern;

	/**
	 * 构造
	 *
	 * @param pattern 正则表达式
	 */
	public RegexDateParser(final Pattern pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean test(final CharSequence source) {
		return ReUtil.isMatch(this.pattern, source);
	}

	@Override
	public Date parse(final CharSequence source) throws DateException {
		final Matcher matcher = this.pattern.matcher(source);
		if(!matcher.matches()){
			throw new DateException("Invalid date string: [{}]", source);
		}

		final DateBuilder dateBuilder = DateBuilder.of();
		// year
		final String year = ReUtil.group(matcher, "year");
		if (StrUtil.isNotEmpty(year)) {
			dateBuilder.setYear(parseYear(year));
		}

		// month
		final String month = ReUtil.group(matcher, "month");
		if (StrUtil.isNotEmpty(month)) {
			dateBuilder.setMonth(parseMonth(month));
		}

		// week
		final String week = ReUtil.group(matcher, "week");
		if (StrUtil.isNotEmpty(week)) {
			dateBuilder.setWeek(parseWeek(week));
		}

		// day
		final String day = ReUtil.group(matcher, "day");
		if (StrUtil.isNotEmpty(day)) {
			dateBuilder.setDay(parseNumberLimit(day, 1, 31));
		}

		// hour
		final String hour = ReUtil.group(matcher, "hour");
		if (StrUtil.isNotEmpty(hour)) {
			dateBuilder.setHour(parseNumberLimit(hour, 0, 23));
		}

		// minute
		final String minute = ReUtil.group(matcher, "minute");
		if (StrUtil.isNotEmpty(minute)) {
			dateBuilder.setMinute(parseNumberLimit(minute, 0, 59));
		}

		// second
		final String second = ReUtil.group(matcher, "second");
		if (StrUtil.isNotEmpty(second)) {
			dateBuilder.setSecond(parseNumberLimit(second, 0, 59));
		}

		// ns
		final String ns = ReUtil.group(matcher, "ns");
		if (StrUtil.isNotEmpty(ns)) {
			dateBuilder.setNs(parseNano(ns));
		}

		// am or pm
		final String m = ReUtil.group(matcher, "m");
		if (StrUtil.isNotEmpty(m)) {
			if ('p' == m.charAt(0)) {
				dateBuilder.setPm(true);
			} else {
				dateBuilder.setAm(true);
			}
		}

		// zero zone offset
		final String zero = ReUtil.group(matcher, "zero");
		if (StrUtil.isNotEmpty(zero)) {
			dateBuilder.setZoneOffsetSetted(true);
			dateBuilder.setZoneOffset(0);
		}

		// zone offset
		final String zoneOffset = ReUtil.group(matcher, "zoneOffset");
		if (StrUtil.isNotEmpty(zoneOffset)) {
			dateBuilder.setZoneOffsetSetted(true);
			dateBuilder.setZoneOffset(parseZoneOffset(zoneOffset));
		}

		// unix时间戳
		final String unixsecond = ReUtil.group(matcher, "unixsecond");
		if (StrUtil.isNotEmpty(unixsecond)) {
			dateBuilder.setUnixsecond(parseLong(unixsecond));
		}

		// 毫秒时间戳
		final String millisecond = ReUtil.group(matcher, "millisecond");
		if (StrUtil.isNotEmpty(millisecond)) {
			return DateUtil.date(parseLong(millisecond));
		}

		return dateBuilder.toDate();
	}

	private static int parseYear(final String year) {
		final int length = year.length();
		switch (length) {
			case 4:
				return Integer.parseInt(year);
			case 2:
				final int num = Integer.parseInt(year);
				return (num > 50 ? 1900 : 2000) + num;
			default:
				throw new DateException("Invalid year: [{}]", year);
		}
	}

	private static int parseMonth(final String month) {
		try {
			final int monthInt = Integer.parseInt(month);
			if (monthInt > 0 && monthInt < 13) {
				return monthInt;
			}
		} catch (final NumberFormatException e) {
			return Month.of(month).getValueBaseOne();
		}

		throw new DateException("Invalid month: [{}]", month);
	}

	private static int parseWeek(final String week){
		return Week.of(week).getIso8601Value();
	}

	private static int parseNumberLimit(final String numberStr, final int minInclude, final int maxInclude) {
		try {
			final int monthInt = Integer.parseInt(numberStr);
			if (monthInt >= minInclude && monthInt <= maxInclude) {
				return monthInt;
			}
		} catch (final NumberFormatException ignored) {
		}
		throw new DateException("Invalid number: [{}]", numberStr);
	}

	private static long parseLong(final String numberStr) {
		try {
			return Long.parseLong(numberStr);
		} catch (final NumberFormatException ignored) {
		}
		throw new DateException("Invalid long: [{}]", numberStr);
	}

	private static int parseInt(final String numberStr, final int from, final int to) {
		try {
			return Integer.parseInt(numberStr.substring(from, to));
		} catch (final NumberFormatException ignored) {
		}
		throw new DateException("Invalid int: [{}]", numberStr);
	}

	private static int parseNano(final String ns) {
		return NSS[ns.length() - 1] * Integer.parseInt(ns);
	}

	/**
	 * 解析时区偏移，类似于'+0800', '+08', '+8:00', '+08:00'
	 * @param zoneOffset 时区偏移
	 * @return 偏移量
	 */
	private int parseZoneOffset(final String zoneOffset) {
		int from = 0;
		final int to = zoneOffset.length();
		final boolean neg = '-' == zoneOffset.charAt(from);
		from++;

		// parse hour
		final int hour;
		if (from + 2 <= to && Character.isDigit(zoneOffset.charAt(from + 1))) {
			hour = parseInt(zoneOffset, from, from + 2);
			from += 2;
		} else {
			hour = parseInt(zoneOffset, from, from + 1);
			from += 1;
		}
		// skip ':' optionally
		if (from + 3 <= to && zoneOffset.charAt(from) == ':') {
			from++;
		}
		// parse minute optionally
		int minute = 0;
		if (from + 2 <= to) {
			minute = parseInt(zoneOffset, from, from + 2);
		}
		return (hour * 60 + minute) * (neg ? -1 : 1);
	}
}
