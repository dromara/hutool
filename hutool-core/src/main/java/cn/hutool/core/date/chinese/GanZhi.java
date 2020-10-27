package cn.hutool.core.date.chinese;

import java.time.LocalDate;

/**
 * 天干地支类
 *
 * @author looly
 * @since 5.4.1
 */
public class GanZhi {

	private static final String[] GAN = new String[]{"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
	private static final String[] ZHI = new String[]{"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};

	/**
	 * 传入 月日的offset 传回干支, 0=甲子
	 *
	 * @param num 月日的offset
	 * @return 干支
	 */
	public static String cyclicalm(int num) {
		return (GAN[num % 10] + ZHI[num % 12]);
	}

	/**
	 * 传入年传回干支
	 *
	 * @param year 农历年
	 * @return 干支
	 * @since 5.4.7
	 */
	public static String getGanzhiOfYear(int year) {
		// 1864年（1900 - 36）是甲子年，用于计算基准的干支年
		return cyclicalm(year - LunarInfo.BASE_YEAR + 36);
	}

	/**
	 * 获取干支月
	 *
	 * @param year  公历年
	 * @param month 公历月，从1开始
	 * @param day   公历日
	 * @return 干支月
	 * @since 5.4.7
	 */
	public static String getGanzhiOfMonth(int year, int month, int day) {
		//返回当月「节」为几日开始
		int firstNode = SolarTerms.getTerm(year, (month * 2 - 1));
		// 依据12节气修正干支月
		int monthOffset = (year - LunarInfo.BASE_YEAR) * 12 + month + 11;
		if(day >= firstNode){
			monthOffset++;
		}
		return cyclicalm(monthOffset);
	}

	/**
	 * 获取干支日
	 *
	 * @param year  公历年
	 * @param month 公历月，从1开始
	 * @param day   公历日
	 * @return 干支
	 * @since 5.4.7
	 */
	public static String getGanzhiOfDay(int year, int month, int day) {
		// 与1970-01-01相差天数，不包括当天
		final long days = LocalDate.of(year, month, day).toEpochDay() - 1;
		//1899-12-21是农历1899年腊月甲子日  40：相差1900-01-31有40天
		return cyclicalm((int)(days - LunarInfo.BASE_DAY + 40));
	}
}
