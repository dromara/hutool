package cn.hutool.cron.pattern.matcher;

import cn.hutool.core.date.Month;

import java.util.List;

/**
 * 每月第几天匹配<br>
 * 考虑每月的天数不同，且存在闰年情况，日匹配单独使用
 *
 * @author Looly
 */
public class DayOfMonthValueMatcher extends BoolArrayValueMatcher {

	private static final int[] LAST_DAYS = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	/**
	 * 构造
	 *
	 * @param intValueList 匹配的日值
	 */
	public DayOfMonthValueMatcher(List<Integer> intValueList) {
		super(intValueList);
	}

	/**
	 * 给定的日期是否匹配当前匹配器
	 *
	 * @param value      被检查的值，此处为日
	 * @param month      实际的月份，从1开始
	 * @param isLeapYear 是否闰年
	 * @return 是否匹配
	 */
	public boolean match(int value, int month, boolean isLeapYear) {
		return (super.match(value) // 在约定日范围内的某一天
				//匹配器中用户定义了最后一天（32表示最后一天）
				|| (value > 27 && match(32) && isLastDayOfMonth(value, month, isLeapYear)));
	}

	/**
	 * 是否为本月最后一天，规则如下：
	 * <pre>
	 * 1、闰年2月匹配是否为29
	 * 2、其它月份是否匹配最后一天的日期（可能为30或者31）
	 * </pre>
	 *
	 * @param value      被检查的值
	 * @param month      月份，从1开始
	 * @param isLeapYear 是否闰年
	 * @return 是否为本月最后一天
	 */
	private static boolean isLastDayOfMonth(int value, int month, boolean isLeapYear) {
		return value == Month.getLastDay(month - 1, isLeapYear);
	}
}
