package cn.hutool.cron.pattern.parser;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronException;
import cn.hutool.cron.pattern.matcher.AlwaysTrueValueMatcher;
import cn.hutool.cron.pattern.matcher.MatcherTable;
import cn.hutool.cron.pattern.matcher.ValueMatcherBuilder;

import java.util.List;

/**
 * 定时任务表达式解析器，用于将表达式字符串解析为{@link MatcherTable}
 *
 * @author looly
 * @since 5.8.0
 */
public class CronPatternParser {

	private static final ValueParser SECOND_VALUE_PARSER = new SecondValueParser();
	private static final ValueParser MINUTE_VALUE_PARSER = new MinuteValueParser();
	private static final ValueParser HOUR_VALUE_PARSER = new HourValueParser();
	private static final ValueParser DAY_OF_MONTH_VALUE_PARSER = new DayOfMonthValueParser();
	private static final ValueParser MONTH_VALUE_PARSER = new MonthValueParser();
	private static final ValueParser DAY_OF_WEEK_VALUE_PARSER = new DayOfWeekValueParser();
	private static final ValueParser YEAR_VALUE_PARSER = new YearValueParser();

	/**
	 * 创建表达式解析器
	 *
	 * @return CronPatternParser
	 */
	public static CronPatternParser create() {
		return new CronPatternParser();
	}

	private MatcherTable matcherTable;

	/**
	 * 解析表达式到匹配表中
	 *
	 * @param cronPattern 复合表达式
	 * @return {@link MatcherTable}
	 */
	public MatcherTable parse(String cronPattern) {
		parseGroupPattern(cronPattern);
		return this.matcherTable;
	}

	/**
	 * 解析复合任务表达式，格式为：
	 * <pre>
	 *     cronA | cronB | ...
	 * </pre>
	 *
	 * @param groupPattern 复合表达式
	 */
	private void parseGroupPattern(String groupPattern) {
		final List<String> patternList = StrUtil.split(groupPattern, '|');
		matcherTable = new MatcherTable(patternList.size());
		for (String pattern : patternList) {
			parseSinglePattern(pattern);
		}
	}

	/**
	 * 解析单一定时任务表达式
	 *
	 * @param pattern 表达式
	 */
	private void parseSinglePattern(String pattern) {
		final String[] parts = pattern.split("\\s");

		int offset = 0;// 偏移量用于兼容Quartz表达式，当表达式有6或7项时，第一项为秒
		if (parts.length == 6 || parts.length == 7) {
			offset = 1;
		} else if (parts.length != 5) {
			throw new CronException("Pattern [{}] is invalid, it must be 5-7 parts!", pattern);
		}

		// 秒
		if (1 == offset) {// 支持秒的表达式
			try {
				matcherTable.secondMatchers.add(ValueMatcherBuilder.build(parts[0], SECOND_VALUE_PARSER));
			} catch (Exception e) {
				throw new CronException(e, "Invalid pattern [{}], parsing 'second' field error!", pattern);
			}
		} else {// 不支持秒的表达式，则第一位按照表达式生成时间的秒数赋值，表示整分匹配
			matcherTable.secondMatchers.add(ValueMatcherBuilder.build(String.valueOf(DateUtil.date().second()), SECOND_VALUE_PARSER));
		}
		// 分
		try {
			matcherTable.minuteMatchers.add(ValueMatcherBuilder.build(parts[offset], MINUTE_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'minute' field error!", pattern);
		}
		// 小时
		try {
			matcherTable.hourMatchers.add(ValueMatcherBuilder.build(parts[1 + offset], HOUR_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'hour' field error!", pattern);
		}
		// 每月第几天
		try {
			matcherTable.dayOfMonthMatchers.add(ValueMatcherBuilder.build(parts[2 + offset], DAY_OF_MONTH_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'day of month' field error!", pattern);
		}
		// 月
		try {
			matcherTable.monthMatchers.add(ValueMatcherBuilder.build(parts[3 + offset], MONTH_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'month' field error!", pattern);
		}
		// 星期几
		try {
			matcherTable.dayOfWeekMatchers.add(ValueMatcherBuilder.build(parts[4 + offset], DAY_OF_WEEK_VALUE_PARSER));
		} catch (Exception e) {
			throw new CronException(e, "Invalid pattern [{}], parsing 'day of week' field error!", pattern);
		}
		// 年
		if (parts.length == 7) {// 支持年的表达式
			try {
				matcherTable.yearMatchers.add(ValueMatcherBuilder.build(parts[6], YEAR_VALUE_PARSER));
			} catch (Exception e) {
				throw new CronException(e, "Invalid pattern [{}], parsing 'year' field error!", pattern);
			}
		} else {// 不支持年的表达式，全部匹配
			matcherTable.yearMatchers.add(new AlwaysTrueValueMatcher());
		}
	}
}
