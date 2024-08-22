/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.cron.pattern;

import org.dromara.hutool.core.date.Week;

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
		final int monthBase1 = calendar.get(Calendar.MONTH) + 1;// 月份从1开始
		final int dayOfWeekBase0 = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 星期从0开始，0和7都表示周日
		final int year = calendar.get(Calendar.YEAR);
		return new int[]{second, minute, hour, dayOfMonth, monthBase1, dayOfWeekBase0, year};
	}
}
