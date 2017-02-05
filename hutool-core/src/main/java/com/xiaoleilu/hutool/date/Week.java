package com.xiaoleilu.hutool.date;

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
	private int value;

	private Week(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
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
