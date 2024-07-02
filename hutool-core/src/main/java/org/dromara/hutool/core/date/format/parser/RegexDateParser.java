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

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.date.DateBuilder;
import org.dromara.hutool.core.date.DateException;
import org.dromara.hutool.core.date.Month;
import org.dromara.hutool.core.date.Week;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.Opt;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.CharUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.text.dfa.WordTree;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用正则列表方式的日期解析器<br>
 * 通过定义若干的日期正则，遍历匹配到给定正则后，按照正则方式解析为日期
 *
 * @author Looly
 */
public class RegexDateParser implements DateParser, Serializable {
	private static final long serialVersionUID = 1L;

	private static final int[] NSS = {100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1};
	private static final Pattern ZONE_OFFSET_PATTERN = Pattern.compile("[-+]\\d{1,2}:?(?:\\d{2})?");
	private static final WordTree ZONE_TREE = WordTree.of(TimeZone.getAvailableIDs());

	/**
	 * 根据给定的正则列表，创建RegexListDateParser
	 *
	 * @param regexes 正则列表，默认忽略大小写
	 * @return RegexListDateParser
	 */
	public static RegexDateParser of(final String... regexes) {
		final List<Pattern> patternList = new ArrayList<>(regexes.length);
		for (final String regex : regexes) {
			patternList.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
		}
		return new RegexDateParser(patternList);
	}

	/**
	 * 根据给定的正则列表，创建RegexListDateParser
	 *
	 * @param patterns 正则列表
	 * @return RegexListDateParser
	 */
	public static RegexDateParser of(final Pattern... patterns) {
		return new RegexDateParser(ListUtil.of(patterns));
	}

	private final List<Pattern> patterns;
	private boolean preferMonthFirst;

	/**
	 * 构造
	 *
	 * @param patterns 正则列表
	 */
	public RegexDateParser(final List<Pattern> patterns) {
		this.patterns = patterns;
	}

	/**
	 * 当用户传入的月和日无法判定默认位置时，设置默认的日期格式为dd/mm还是mm/dd
	 *
	 * @param preferMonthFirst {@code true}默认为mm/dd，否则dd/mm
	 */
	public void setPreferMonthFirst(final boolean preferMonthFirst) {
		this.preferMonthFirst = preferMonthFirst;
	}

	/**
	 * 新增自定义日期正则
	 *
	 * @param regex 日期正则
	 * @return this
	 */
	public RegexDateParser addRegex(final String regex) {
		// 日期正则忽略大小写
		return addPattern(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
	}

	/**
	 * 新增自定义日期正则
	 *
	 * @param pattern 日期正则
	 * @return this
	 */
	public RegexDateParser addPattern(final Pattern pattern) {
		this.patterns.add(pattern);
		return this;
	}

	@Override
	public Date parse(final CharSequence source) throws DateException {
		Assert.notBlank(source, "Date str must be not blank!");
		return parseToBuilder(source).toDate();
	}

	/**
	 * 解析日期，结果为{@link LocalDateTime}
	 *
	 * @param source 日期字符串
	 * @return {@link LocalDateTime}
	 * @throws DateException 解析异常
	 */
	public LocalDateTime parseToLocalDateTime(final CharSequence source) throws DateException {
		Assert.notBlank(source, "Date str must be not blank!");
		return parseToBuilder(source).toLocalDateTime();
	}

	/**
	 * 解析日期，结果为{@link OffsetDateTime}
	 *
	 * @param source 日期字符串
	 * @return {@link OffsetDateTime}
	 * @throws DateException 解析异常
	 */
	public OffsetDateTime parseToOffsetDateTime(final CharSequence source) throws DateException {
		Assert.notBlank(source, "Date str must be not blank!");
		return parseToBuilder(source).toOffsetDateTime();
	}

	/**
	 * 解析日期
	 *
	 * @param source 日期字符串
	 * @return DateBuilder
	 * @throws DateException 日期解析异常
	 */
	private DateBuilder parseToBuilder(final CharSequence source) throws DateException {
		final DateBuilder dateBuilder = DateBuilder.of();
		Matcher matcher;
		for (final Pattern pattern : this.patterns) {
			matcher = pattern.matcher(source);
			if (matcher.matches()) {
				parse(matcher, dateBuilder);
				return dateBuilder;
			}
		}

		throw new DateException("No valid pattern for date string: [{}]", source);
	}

	/**
	 * 解析日期
	 *
	 * @param matcher 正则匹配器
	 * @throws DateException 日期解析异常
	 */
	private void parse(final Matcher matcher, final DateBuilder dateBuilder) throws DateException {

		// 纯数字格式
		final String number = ReUtil.group(matcher, "number");
		if (StrUtil.isNotEmpty(number)) {
			parseNumberDate(number, dateBuilder);
			return;
		}

		// 毫秒时间戳
		final String millisecond = ReUtil.group(matcher, "millisecond");
		if (StrUtil.isNotEmpty(millisecond)) {
			dateBuilder.setMillisecond(parseLong(millisecond));
			return;
		}

		// year
		Opt.ofNullable(ReUtil.group(matcher, "year")).ifPresent((year) -> dateBuilder.setYear(parseYear(year)));
		// dayOrMonth, dd/mm or mm/dd
		Opt.ofNullable(ReUtil.group(matcher, "dayOrMonth")).ifPresent((dayOrMonth) -> parseDayOrMonth(dayOrMonth, dateBuilder, preferMonthFirst));
		// month
		Opt.ofNullable(ReUtil.group(matcher, "month")).ifPresent((month) -> dateBuilder.setMonth(parseMonth(month)));
		// week
		Opt.ofNullable(ReUtil.group(matcher, "week")).ifPresent((week) -> dateBuilder.setWeek(parseWeek(week)));
		// day
		Opt.ofNullable(ReUtil.group(matcher, "day")).ifPresent((day) -> dateBuilder.setDay(parseNumberLimit(day, 1, 31)));
		// hour
		Opt.ofNullable(ReUtil.group(matcher, "hour")).ifPresent((hour) -> dateBuilder.setHour(parseNumberLimit(hour, 0, 23)));
		// minute
		Opt.ofNullable(ReUtil.group(matcher, "minute")).ifPresent((minute) -> dateBuilder.setMinute(parseNumberLimit(minute, 0, 59)));
		// second
		Opt.ofNullable(ReUtil.group(matcher, "second")).ifPresent((second) -> dateBuilder.setSecond(parseNumberLimit(second, 0, 59)));
		// ns
		Opt.ofNullable(ReUtil.group(matcher, "ns")).ifPresent((ns) -> dateBuilder.setNs(parseNano(ns)));
		// am or pm
		Opt.ofNullable(ReUtil.group(matcher, "m")).ifPresent((m) -> {
			if (CharUtil.equals('p', m.charAt(0), true)) {
				dateBuilder.setPm(true);
			} else {
				dateBuilder.setAm(true);
			}
		});

		// zero zone offset
		Opt.ofNullable(ReUtil.group(matcher, "zero")).ifPresent((zero) -> {
			dateBuilder.setZoneOffsetSetted(true);
			dateBuilder.setZoneOffset(0);
		});

		// zone（包括可时区名称、时区偏移等信息，综合解析）
		Opt.ofNullable(ReUtil.group(matcher, "zone")).ifPresent((zoneOffset) -> {
			parseZone(zoneOffset, dateBuilder);
		});

		// zone offset
		Opt.ofNullable(ReUtil.group(matcher, "zoneOffset")).ifPresent((zoneOffset) -> {
			dateBuilder.setZoneOffsetSetted(true);
			dateBuilder.setZoneOffset(parseZoneOffset(zoneOffset));
		});

		// unix时间戳，可能有NS
		Opt.ofNullable(ReUtil.group(matcher, "unixsecond")).ifPresent((unixsecond) -> {
			dateBuilder.setUnixsecond(parseLong(unixsecond));
		});
	}

	/**
	 * 解析纯数字型的日期
	 *
	 * @param number      纯数字
	 * @param dateBuilder {@link DateBuilder}
	 */
	private static void parseNumberDate(final String number, final DateBuilder dateBuilder) {
		final int length = number.length();
		switch (length) {
			case 4:
				// yyyy
				dateBuilder.setYear(Integer.parseInt(number));
				break;
			case 6:
				// yyyyMM
				dateBuilder.setYear(parseInt(number, 0, 4));
				dateBuilder.setMonth(parseInt(number, 4, 6));
				break;
			case 8:
				// yyyyMMdd
				dateBuilder.setYear(parseInt(number, 0, 4));
				dateBuilder.setMonth(parseInt(number, 4, 6));
				dateBuilder.setDay(parseInt(number, 6, 8));
				break;
			case 14:
				dateBuilder.setYear(parseInt(number, 0, 4));
				dateBuilder.setMonth(parseInt(number, 4, 6));
				dateBuilder.setDay(parseInt(number, 6, 8));
				dateBuilder.setHour(parseInt(number, 8, 10));
				dateBuilder.setMinute(parseInt(number, 10, 12));
				dateBuilder.setSecond(parseInt(number, 12, 14));
				break;
			case 10:
				// unixtime(10)
				dateBuilder.setUnixsecond(parseLong(number));
				break;
			case 13:
				// millisecond(13)
				dateBuilder.setMillisecond(parseLong(number));
				break;
			case 16:
				// microsecond(16)
				dateBuilder.setUnixsecond(parseLong(number.substring(0, 10)));
				dateBuilder.setNs(parseInt(number, 10, 16));
				break;
			case 19:
				// nanosecond(19)
				dateBuilder.setUnixsecond(parseLong(number.substring(0, 10)));
				dateBuilder.setNs(parseInt(number, 10, 19));
				break;
		}
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

	/**
	 * 解析日期中的日或月，类似于dd/mm或mm/dd格式
	 *
	 * @param dayOrMonth       日期中的日或月
	 * @param dateBuilder      {@link DateBuilder}
	 * @param preferMonthFirst 是否月份在前
	 */
	private static void parseDayOrMonth(final String dayOrMonth, final DateBuilder dateBuilder, final boolean preferMonthFirst) {
		final char next = dayOrMonth.charAt(1);
		final int a;
		final int b;
		if (next < '0' || next > '9') {
			// d/m
			a = parseInt(dayOrMonth, 0, 1);
			b = parseInt(dayOrMonth, 2, dayOrMonth.length());
		} else {
			// dd/mm
			a = parseInt(dayOrMonth, 0, 2);
			b = parseInt(dayOrMonth, 3, dayOrMonth.length());
		}

		if (a > 31 || b > 31 || a == 0 || b == 0 || (a > 12 && b > 12)) {
			throw new DateException("Invalid DayOrMonth : {}", dayOrMonth);
		}

		if (b > 12 || (preferMonthFirst && a <= 12)) {
			dateBuilder.setMonth(a);
			dateBuilder.setDay(b);
		} else {
			dateBuilder.setMonth(b);
			dateBuilder.setDay(a);
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

	private static int parseWeek(final String week) {
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
	 * 解析时区，包括时区偏移和时区名称
	 *
	 * @param zone        时区
	 * @param dateBuilder 日期时间对象
	 */
	private static void parseZone(final String zone, final DateBuilder dateBuilder) {
		// 检查是否直接定义了时区偏移
		final String zoneOffset = ReUtil.getGroup0(ZONE_OFFSET_PATTERN, zone);
		if (StrUtil.isNotBlank(zoneOffset)) {
			dateBuilder.setZoneOffsetSetted(true);
			dateBuilder.setZoneOffset(parseZoneOffset(zoneOffset));
			return;
		}

		// 检查是否定义了时区名称
		final String zoneName = ZONE_TREE.match(zone);
		if (StrUtil.isNotBlank(zoneName)) {
			dateBuilder.setZoneOffsetSetted(true);
			dateBuilder.setZone(TimeZone.getTimeZone(zoneName));
		}
	}

	/**
	 * 解析时区偏移，类似于'+0800', '+08', '+8:00', '+08:00'
	 *
	 * @param zoneOffset 时区偏移
	 * @return 偏移量
	 */
	private static int parseZoneOffset(final String zoneOffset) {
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
