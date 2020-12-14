package cn.hutool.core.date;

import java.util.Calendar;

/**
 * 月份枚举<br>
 * 与Calendar中的月份int值对应
 *
 * @author Looly
 * @see Calendar#JANUARY
 * @see Calendar#FEBRUARY
 * @see Calendar#MARCH
 * @see Calendar#APRIL
 * @see Calendar#MAY
 * @see Calendar#JUNE
 * @see Calendar#JULY
 * @see Calendar#AUGUST
 * @see Calendar#SEPTEMBER
 * @see Calendar#OCTOBER
 * @see Calendar#NOVEMBER
 * @see Calendar#DECEMBER
 * @see Calendar#UNDECIMBER
 */
public enum Month {
	/**
	 * 一月
	 */
	JANUARY(Calendar.JANUARY),
	/**
	 * 二月
	 */
	FEBRUARY(Calendar.FEBRUARY),
	/**
	 * 三月
	 */
	MARCH(Calendar.MARCH),
	/**
	 * 四月
	 */
	APRIL(Calendar.APRIL),
	/**
	 * 五月
	 */
	MAY(Calendar.MAY),
	/**
	 * 六月
	 */
	JUNE(Calendar.JUNE),
	/**
	 * 七月
	 */
	JULY(Calendar.JULY),
	/**
	 * 八月
	 */
	AUGUST(Calendar.AUGUST),
	/**
	 * 九月
	 */
	SEPTEMBER(Calendar.SEPTEMBER),
	/**
	 * 十月
	 */
	OCTOBER(Calendar.OCTOBER),
	/**
	 * 十一月
	 */
	NOVEMBER(Calendar.NOVEMBER),
	/**
	 * 十二月
	 */
	DECEMBER(Calendar.DECEMBER),
	/**
	 * 十三月，仅用于农历
	 */
	UNDECIMBER(Calendar.UNDECIMBER);

	// ---------------------------------------------------------------
	private static final int[] DAYS_OF_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31, -1};

	/**
	 * 对应值，见{@link Calendar}
	 */
	private final int value;

	/**
	 * 构造
	 *
	 * @param value 对应值，见{@link Calendar}
	 */
	Month(int value) {
		this.value = value;
	}

	/**
	 * 获取{@link Calendar}中的对应值
	 *
	 * @return {@link Calendar}中的对应值
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * 获取此月份最后一天的值，不支持的月份（例如UNDECIMBER）返回-1
	 *
	 * @param isLeapYear 是否闰年
	 * @return 此月份最后一天的值
	 */
	public int getLastDay(boolean isLeapYear) {
		return getLastDay(this.value, isLeapYear);
	}

	/**
	 * 将 {@link Calendar}月份相关值转换为Month枚举对象<br>
	 *
	 * @param calendarMonthIntValue Calendar中关于Month的int值
	 * @return Month
	 * @see Calendar#JANUARY
	 * @see Calendar#FEBRUARY
	 * @see Calendar#MARCH
	 * @see Calendar#APRIL
	 * @see Calendar#MAY
	 * @see Calendar#JUNE
	 * @see Calendar#JULY
	 * @see Calendar#AUGUST
	 * @see Calendar#SEPTEMBER
	 * @see Calendar#OCTOBER
	 * @see Calendar#NOVEMBER
	 * @see Calendar#DECEMBER
	 * @see Calendar#UNDECIMBER
	 */
	public static Month of(int calendarMonthIntValue) {
		switch (calendarMonthIntValue) {
			case Calendar.JANUARY:
				return JANUARY;
			case Calendar.FEBRUARY:
				return FEBRUARY;
			case Calendar.MARCH:
				return MARCH;
			case Calendar.APRIL:
				return APRIL;
			case Calendar.MAY:
				return MAY;
			case Calendar.JUNE:
				return JUNE;
			case Calendar.JULY:
				return JULY;
			case Calendar.AUGUST:
				return AUGUST;
			case Calendar.SEPTEMBER:
				return SEPTEMBER;
			case Calendar.OCTOBER:
				return OCTOBER;
			case Calendar.NOVEMBER:
				return NOVEMBER;
			case Calendar.DECEMBER:
				return DECEMBER;
			case Calendar.UNDECIMBER:
				return UNDECIMBER;
			default:
				return null;
		}
	}

	/**
	 * 获得指定月的最后一天
	 * @param month 月份，从0开始
	 * @param isLeapYear 是否为闰年，闰年只对二月有影响
	 * @return 最后一天，可能为28,29,30,31
	 * @since 5.4.7
	 */
	public static int getLastDay(int month, boolean isLeapYear){
		int lastDay = DAYS_OF_MONTH[month];
		if (isLeapYear && Calendar.FEBRUARY == month){
			// 二月
			lastDay += 1;
		}
		return lastDay;
	}
}
