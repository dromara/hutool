package cn.hutool.cron.pattern.matcher;

import java.util.List;

/**
 * 每月第几天匹配<br>
 * 考虑每月的天数不同，切存在闰年情况，日匹配单独使用
 * @author Looly
 *
 */
public class DayOfMonthValueMatcher extends BoolArrayValueMatcher{
	
	private static final int[] LAST_DAYS = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public DayOfMonthValueMatcher(List<Integer> intValueList) {
		super(intValueList);
	}
	
	/**
	 * 是否匹配
	 * @param value 被检查的值
	 * @param month 月份
	 * @param isLeapYear 是否闰年
	 * @return 是否匹配
	 */
	public boolean match(int value, int month, boolean isLeapYear) {
		return (super.match(value) || (value > 27 && match(32) && isLastDayOfMonth(value, month, isLeapYear)));
	}

	/**
	 * 是否为本月最后一天
	 * @param value 被检查的值
	 * @param month 月份
	 * @param isLeapYear 是否闰年
	 * @return 是否为本月最后一天
	 */
	private static boolean isLastDayOfMonth(int value, int month, boolean isLeapYear) {
		if (isLeapYear && month == 2) {
			return value == 29;
		} else {
			return value == LAST_DAYS[month - 1];
		}
	}
}
