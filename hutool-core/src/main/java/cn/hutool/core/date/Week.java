package cn.hutool.core.date;

import java.util.Calendar;

/**
 * 星期枚举<br>
 * 与Calendar中的星期int值对应
 * 
 * @see #SUNDAY
 * @see #MONDAY
 * @see #TUESDAY
 * @see #WEDNESDAY
 * @see #THURSDAY
 * @see #FRIDAY
 * @see #SATURDAY
 * 
 * @author Looly
 *
 */
public enum Week {

	/** 周日 */
	SUNDAY(Calendar.SUNDAY),
	/** 周一 */
	MONDAY(Calendar.MONDAY),
	/** 周二 */
	TUESDAY(Calendar.TUESDAY),
	/** 周三 */
	WEDNESDAY(Calendar.WEDNESDAY),
	/** 周四 */
	THURSDAY(Calendar.THURSDAY),
	/** 周五 */
	FRIDAY(Calendar.FRIDAY),
	/** 周六 */
	SATURDAY(Calendar.SATURDAY);

	// ---------------------------------------------------------------
	/** 星期对应{@link Calendar} 中的Week值 */
	private final int value;

	/**
	 * 构造
	 * 
	 * @param value 星期对应{@link Calendar} 中的Week值
	 */
	Week(int value) {
		this.value = value;
	}

	/**
	 * 获得星期对应{@link Calendar} 中的Week值
	 * 
	 * @return 星期对应{@link Calendar} 中的Week值
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * 转换为中文名
	 * 
	 * @return 星期的中文名
	 * @since 3.3.0
	 */
	public String toChinese() {
		return toChinese("星期");
	}

	/**
	 * 转换为中文名
	 * 
	 * @param weekNamePre 表示星期的前缀，例如前缀为“星期”，则返回结果为“星期一”；前缀为”周“，结果为“周一”
	 * @return 星期的中文名
	 * @since 4.0.11
	 */
	public String toChinese(String weekNamePre) {
		switch (this) {
		case SUNDAY:
			return weekNamePre + "日";
		case MONDAY:
			return weekNamePre + "一";
		case TUESDAY:
			return weekNamePre + "二";
		case WEDNESDAY:
			return weekNamePre + "三";
		case THURSDAY:
			return weekNamePre + "四";
		case FRIDAY:
			return weekNamePre + "五";
		case SATURDAY:
			return weekNamePre + "六";
		default:
			return null;
		}
	}

	/**
	 * 将 {@link Calendar}星期相关值转换为Week枚举对象<br>
	 * 
	 * @see #SUNDAY
	 * @see #MONDAY
	 * @see #TUESDAY
	 * @see #WEDNESDAY
	 * @see #THURSDAY
	 * @see #FRIDAY
	 * @see #SATURDAY
	 * 
	 * @param calendarWeekIntValue Calendar中关于Week的int值
	 * @return {@link Week}
	 */
	public static Week of(int calendarWeekIntValue) {
		switch (calendarWeekIntValue) {
		case Calendar.SUNDAY:
			return SUNDAY;
		case Calendar.MONDAY:
			return MONDAY;
		case Calendar.TUESDAY:
			return TUESDAY;
		case Calendar.WEDNESDAY:
			return WEDNESDAY;
		case Calendar.THURSDAY:
			return THURSDAY;
		case Calendar.FRIDAY:
			return FRIDAY;
		case Calendar.SATURDAY:
			return SATURDAY;
		default:
			return null;
		}
	}
}
