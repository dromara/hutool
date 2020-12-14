package cn.hutool.core.date.chinese;

/**
 * 农历月份表示
 *
 * @author looly
 * @since 5.4.1
 */
public class ChineseMonth {

	private static final String[] MONTH_NAME = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
	private static final String[] MONTH_NAME_TRADITIONAL = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "腊"};

	/**
	 * 当前农历月份是否为闰月
	 *
	 * @param year  农历年
	 * @param month 农历月
	 * @return 是否为闰月
	 * @since 5.4.2
	 */
	public static boolean isLeapMonth(int year, int month) {
		return month == LunarInfo.leapMonth(year);
	}

	/**
	 * 获得农历月称呼<br>
	 * 当为传统表示时，表示为二月，腊月，或者润正月等
	 * 当为非传统表示时，二月，十二月，或者润一月等
	 *
	 * @param isLeapMonth   是否闰月
	 * @param month         月份，从1开始
	 * @param isTraditional 是否传统表示，例如一月传统表示为正月
	 * @return 返回农历月份称呼
	 */
	public static String getChineseMonthName(boolean isLeapMonth, int month, boolean isTraditional) {
		return (isLeapMonth ? "闰" : "") + (isTraditional ? MONTH_NAME_TRADITIONAL : MONTH_NAME)[month - 1] + "月";
	}
}
