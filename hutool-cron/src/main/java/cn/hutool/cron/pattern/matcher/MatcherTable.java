package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.mutable.MutableBool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

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

	/**
	 * 获取下一个最近的匹配日期时间
	 *
	 * @param second     秒
	 * @param minute     分
	 * @param hour       时
	 * @param dayOfMonth 天
	 * @param month      月（从0开始）
	 * @param dayOfWeek  周
	 * @param year       年
	 * @param zone       时区
	 * @return {@link Calendar}
	 */
	public Calendar nextMatchAfter(int second, int minute, int hour, int dayOfMonth, int month, int dayOfWeek, int year, TimeZone zone) {
		List<Calendar> nextMatchs = new ArrayList<>(second);
		for (DateTimeMatcher matcher : matchers) {
			nextMatchs.add(singleNextMatchAfter(matcher, second, minute, hour,
					dayOfMonth, month, dayOfWeek, year, zone));
		}
		// 返回匹配到的最早日期
		return CollUtil.min(nextMatchs);
	}

	/**
	 * 获取下一个匹配日期时间
	 *
	 * @param matcher    匹配器
	 * @param second     秒
	 * @param minute     分
	 * @param hour       时
	 * @param dayOfMonth 天
	 * @param month      月（从0开始）
	 * @param dayOfWeek  周
	 * @param year       年
	 * @param zone       时区
	 * @return {@link Calendar}
	 */
	private static Calendar singleNextMatchAfter(DateTimeMatcher matcher, int second, int minute, int hour,
												 int dayOfMonth, int month, int dayOfWeek, int year, TimeZone zone) {

		Calendar calendar = Calendar.getInstance(zone);

		// 上一个字段不一致，说明产生了新值，下一个字段使用最小值
		MutableBool isNextEquals = new MutableBool(true);
		// 年
		final int nextYear = nextAfter(matcher.yearMatcher, year, isNextEquals);
		calendar.set(Calendar.YEAR, nextYear);

		// 周
		final int nextDayOfWeek = nextAfter(matcher.dayOfWeekMatcher, dayOfWeek, isNextEquals);
		calendar.set(Calendar.DAY_OF_WEEK, nextDayOfWeek);

		// 月
		final int nextMonth = nextAfter(matcher.monthMatcher, month + 1, isNextEquals);
		calendar.set(Calendar.MONTH, nextMonth - 1);

		// 日
		final int nextDayOfMonth = nextAfter(matcher.dayOfMonthMatcher, dayOfMonth, isNextEquals);
		calendar.set(Calendar.DAY_OF_MONTH, nextDayOfMonth);

		// 时
		final int nextHour = nextAfter(matcher.hourMatcher, hour, isNextEquals);
		calendar.set(Calendar.HOUR, nextHour);

		// 分
		int nextMinute = nextAfter(matcher.minuteMatcher, minute, isNextEquals);
		calendar.set(Calendar.MINUTE, nextMinute);

		// 秒
		final int nextSecond = nextAfter(matcher.secondMatcher, second, isNextEquals);
		calendar.set(Calendar.SECOND, nextSecond);

		Console.log(nextYear, nextDayOfWeek, nextMonth, nextDayOfMonth, nextHour, nextMinute, nextSecond);
		return calendar;
	}

	/**
	 * 获取对应字段匹配器的下一个值
	 *
	 * @param matcher      匹配器
	 * @param value        值
	 * @param isNextEquals 是否下一个值和值相同。不同获取初始值，相同获取下一值，然后修改。
	 * @return 下一个值，-1标识匹配所有值的情况，应获取整个字段的最小值
	 */
	private static int nextAfter(ValueMatcher matcher, int value, MutableBool isNextEquals) {
		int nextValue;
		if (isNextEquals.get()) {
			// 上一层级得到相同值，下级获取下个值
			nextValue = matcher.nextAfter(value);
			isNextEquals.set(nextValue == value);
		} else {
			// 上一层级的值得到了不同值，下级的所有值使用最小值
			if (matcher instanceof AlwaysTrueValueMatcher) {
				nextValue = value;
			} else if (matcher instanceof BoolArrayValueMatcher) {
				nextValue = ((BoolArrayValueMatcher) matcher).getMinValue();
			} else {
				throw new IllegalArgumentException("Invalid matcher: " + matcher.getClass().getName());
			}
		}
		return nextValue;
	}

	/**
	 * 给定时间是否匹配定时任务表达式
	 *
	 * @param second     秒数，-1表示不匹配此项
	 * @param minute     分钟
	 * @param hour       小时
	 * @param dayOfMonth 天
	 * @param month      月，从1开始
	 * @param dayOfWeek  周，从0开始，0和7都表示周日
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
