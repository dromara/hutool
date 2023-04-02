/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.pattern;

import org.dromara.hutool.date.Week;

import java.time.LocalDateTime;
import java.util.Calendar;

/**
 * 表达式工具，内部使用
 *
 * @author looly
 * @since 5.8.0
 */
class PatternUtil {

	/**
	 * 获取处理后的字段列表<br>
	 * 月份从1开始，周从0开始
	 *
	 * @param dateTime      {@link Calendar}
	 * @param isMatchSecond 是否匹配秒，{@link false}则秒返回-1
	 * @return 字段值列表
	 * @since 5.8.0
	 */
	static int[] getFields(final LocalDateTime dateTime, final boolean isMatchSecond) {
		final int second = isMatchSecond ? dateTime.getSecond() : -1;
		final int minute = dateTime.getMinute();
		final int hour = dateTime.getHour();
		final int dayOfMonth = dateTime.getDayOfMonth();
		final int month = dateTime.getMonthValue();// 月份从1开始
		final int dayOfWeek = Week.of(dateTime.getDayOfWeek()).getValue() - 1; // 星期从0开始，0和7都表示周日
		final int year = dateTime.getYear();
		return new int[]{second, minute, hour, dayOfMonth, month, dayOfWeek, year};
	}

	/**
	 * 获取处理后的字段列表<br>
	 * 月份从1开始，周从0开始
	 *
	 * @param calendar      {@link Calendar}
	 * @param isMatchSecond 是否匹配秒，{@link false}则秒返回-1
	 * @return 字段值列表
	 * @since 5.8.0
	 */
	static int[] getFields(final Calendar calendar, final boolean isMatchSecond) {
		final int second = isMatchSecond ? calendar.get(Calendar.SECOND) : -1;
		final int minute = calendar.get(Calendar.MINUTE);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		final int month = calendar.get(Calendar.MONTH) + 1;// 月份从1开始
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 星期从0开始，0和7都表示周日
		final int year = calendar.get(Calendar.YEAR);
		return new int[]{second, minute, hour, dayOfMonth, month, dayOfWeek, year};
	}
}
