package cn.hutool.cron.pattern.matcher;

import java.time.Year;

/**
 * 日期和时间的单一匹配器
 *
 * @author looly
 * @since 5.8.0
 */
public class DateTimeMatcher {

	/**
	 * 秒字段匹配列表
	 */
	final ValueMatcher secondMatcher;
	/**
	 * 分字段匹配列表
	 */
	final ValueMatcher minuteMatcher;
	/**
	 * 时字段匹配列表
	 */
	final ValueMatcher hourMatcher;
	/**
	 * 每月几号字段匹配列表
	 */
	final ValueMatcher dayOfMonthMatcher;
	/**
	 * 月字段匹配列表
	 */
	final ValueMatcher monthMatcher;
	/**
	 * 星期字段匹配列表
	 */
	final ValueMatcher dayOfWeekMatcher;
	/**
	 * 年字段匹配列表
	 */
	final ValueMatcher yearMatcher;

	public DateTimeMatcher(ValueMatcher secondMatcher,
						   ValueMatcher minuteMatcher,
						   ValueMatcher hourMatchers,
						   ValueMatcher dayOfMonthMatchers,
						   ValueMatcher monthMatchers,
						   ValueMatcher dayOfWeekMatchers,
						   ValueMatcher yearMatchers) {

		this.secondMatcher = secondMatcher;
		this.minuteMatcher = minuteMatcher;
		this.hourMatcher = hourMatchers;
		this.dayOfMonthMatcher = dayOfMonthMatchers;
		this.monthMatcher = monthMatchers;
		this.dayOfWeekMatcher = dayOfWeekMatchers;
		this.yearMatcher = yearMatchers;
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
		return ((second < 0) || secondMatcher.match(second)) // 匹配秒（非秒匹配模式下始终返回true）
				&& minuteMatcher.match(minute)// 匹配分
				&& hourMatcher.match(hour)// 匹配时
				&& isMatchDayOfMonth(dayOfMonthMatcher, dayOfMonth, month, Year.isLeap(year))// 匹配日
				&& monthMatcher.match(month) // 匹配月
				&& dayOfWeekMatcher.match(dayOfWeek)// 匹配周
				&& yearMatcher.match(year);// 匹配年
	}

	/**
	 * 是否匹配日（指定月份的第几天）
	 *
	 * @param matcher {@link ValueMatcher}
	 * @param dayOfMonth 日
	 * @param month 月
	 * @param isLeapYear 是否闰年
	 * @return 是否匹配
	 */
	private static boolean isMatchDayOfMonth(ValueMatcher matcher, int dayOfMonth, int month, boolean isLeapYear) {
		return ((matcher instanceof DayOfMonthValueMatcher) //
				? ((DayOfMonthValueMatcher) matcher).match(dayOfMonth, month, isLeapYear) //
				: matcher.match(dayOfMonth));
	}
}
