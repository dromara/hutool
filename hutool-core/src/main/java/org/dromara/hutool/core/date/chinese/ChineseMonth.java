/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.date.chinese;

/**
 * 农历月份表示
 *
 * @author looly
 * @since 5.4.1
 */
public class ChineseMonth {

	private static final String[] MONTH_NAME = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
	private static final String[] MONTH_NAME_TRADITIONAL = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "寒", "冬", "腊"};

	/**
	 * 当前农历月份是否为闰月
	 *
	 * @param year  农历年
	 * @param month 农历月
	 * @return 是否为闰月
	 * @since 5.4.2
	 */
	public static boolean isLeapMonth(final int year, final int month) {
		return month == LunarInfo.leapMonth(year);
	}

	/**
	 * 获得农历月称呼<br>
	 * 当为传统表示时，表示为二月，腊月，或者润正月等
	 * 当为非传统表示时，二月，十二月，或者润一月等
	 *
	 * @param isLeapMonth   是否闰月
	 * @param month         月份，从1开始，如果是闰月，应传入需要显示的月份
	 * @param isTraditional 是否传统表示，例如一月传统表示为正月
	 * @return 返回农历月份称呼
	 */
	public static String getChineseMonthName(final boolean isLeapMonth, final int month, final boolean isTraditional) {
		return (isLeapMonth ? "闰" : "") + (isTraditional ? MONTH_NAME_TRADITIONAL : MONTH_NAME)[month - 1] + "月";
	}
}
