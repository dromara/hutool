package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.collection.CollUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 时间匹配表，用于存放定时任务表达式解析后的结构信息
 *
 * @author looly
 * @since 5.8.0
 */
public class MatcherTable {

	/**
	 * 秒字段匹配列表
	 */
	public final List<DateTimeMatcher> matchers;

	/**
	 * 构造
	 *
	 * @param size 表达式个数，用于表示复合表达式中单个表达式个数
	 */
	public MatcherTable(int size) {
		matchers = new ArrayList<>(size);
	}

	public LocalDateTime nextMatchAfter(int second, int minute, int hour, int dayOfMonth, int month, int dayOfWeek, int year) {
		List<LocalDateTime> nextMatchs = new ArrayList<>(second);
		for (DateTimeMatcher matcher : matchers) {
			nextMatchs.add(singleNextMatchAfter(matcher, second, minute, hour,
					dayOfMonth, month, dayOfWeek, year));
		}
		// 返回最先匹配到的日期
		return CollUtil.min(nextMatchs);
	}

	private static LocalDateTime singleNextMatchAfter(DateTimeMatcher matcher, int second, int minute, int hour,
											   int dayOfMonth, int month, int dayOfWeek, int year){
		boolean isNextNotEquals = true;
		// 年
		final int nextYear = matcher.yearMatcher.nextAfter(year);
		isNextNotEquals &= (year != nextYear);

		// 周
		int nextDayOfWeek;
		if(isNextNotEquals){
			// 上一个字段不一致，说明产生了新值，本字段使用最小值
			nextDayOfWeek = ((BoolArrayValueMatcher)matcher.dayOfWeekMatcher).getMinValue();
		}else{
			nextDayOfWeek = matcher.dayOfWeekMatcher.nextAfter(dayOfWeek);
			isNextNotEquals &= (dayOfWeek != nextDayOfWeek);
		}

		// 月
		int nextMonth;
		if(isNextNotEquals){
			// 上一个字段不一致，说明产生了新值，本字段使用最小值
			nextMonth = ((BoolArrayValueMatcher)matcher.monthMatcher).getMinValue();
		}else{
			nextMonth = matcher.monthMatcher.nextAfter(dayOfWeek);
			isNextNotEquals &= (month != nextMonth);
		}

		// 日
		int nextDayOfMonth;
		if(isNextNotEquals){
			// 上一个字段不一致，说明产生了新值，本字段使用最小值
			nextDayOfMonth = ((BoolArrayValueMatcher)matcher.dayOfMonthMatcher).getMinValue();
		}else{
			nextDayOfMonth = matcher.dayOfMonthMatcher.nextAfter(dayOfWeek);
			isNextNotEquals &= (dayOfMonth != nextDayOfMonth);
		}

		return null;
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 *
	 * @param second     秒数，-1表示不匹配此项
	 * @param minute     分钟
	 * @param hour       小时
	 * @param dayOfMonth 天
	 * @param month      月
	 * @param dayOfWeek  周几
	 * @param year       年
	 * @return 如果匹配返回 {@code true}, 否则返回 {@code false}
	 */
	public boolean match(int second, int minute, int hour, int dayOfMonth, int month, int dayOfWeek, int year) {
		for (DateTimeMatcher matcher : matchers) {
			if (matcher.match(second, minute, hour, dayOfMonth, month, dayOfWeek, year)) {
				return true;
			}
		}
		return false;
	}
}
