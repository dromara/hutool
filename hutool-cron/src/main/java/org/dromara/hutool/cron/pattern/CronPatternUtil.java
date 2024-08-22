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

import org.dromara.hutool.core.date.CalendarUtil;
import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 定时任务表达式工具类
 *
 * @author looly
 *
 */
public class CronPatternUtil {

	/**
	 * 列举指定日期之后（到开始日期对应年年底）内第一个匹配表达式的日期
	 *
	 * @param pattern 表达式
	 * @param start 起始时间
	 * @return 日期
	 * @since 4.5.8
	 */
	public static Date nextDateAfter(final CronPattern pattern, final Date start) {
		return DateUtil.date(pattern.nextMatchAfter(CalendarUtil.calendar(start)));
	}

	/**
	 * 列举指定日期之后（到开始日期对应年年底）内所有匹配表达式的日期
	 *
	 * @param patternStr 表达式字符串
	 * @param start 起始时间
	 * @param count 列举数量
	 * @return 日期列表
	 */
	public static List<Date> matchedDates(final String patternStr, final Date start, final int count) {
		return matchedDates(patternStr, start, DateUtil.endOfYear(start, false), count);
	}

	/**
	 * 列举指定日期范围内所有匹配表达式的日期
	 *
	 * @param patternStr 表达式字符串
	 * @param start 起始时间
	 * @param end 结束时间
	 * @param count 列举数量
	 * @return 日期列表
	 */
	public static List<Date> matchedDates(final String patternStr, final Date start, final Date end, final int count) {
		return matchedDates(patternStr, start.getTime(), end.getTime(), count);
	}

	/**
	 * 列举指定日期范围内所有匹配表达式的日期
	 *
	 * @param patternStr 表达式字符串
	 * @param start 起始时间
	 * @param end 结束时间
	 * @param count 列举数量
	 * @return 日期列表
	 */
	public static List<Date> matchedDates(final String patternStr, final long start, final long end, final int count) {
		return matchedDates(new CronPattern(patternStr), start, end, count);
	}

	/**
	 * 列举指定日期范围内所有匹配表达式的日期
	 *
	 * @param pattern 表达式
	 * @param start 起始时间
	 * @param end 结束时间
	 * @param count 列举数量
	 * @return 日期列表
	 */
	public static List<Date> matchedDates(final CronPattern pattern, final long start, final long end, final int count) {
		Assert.isTrue(start < end, "Start date is later than end !");

		final List<Date> result = new ArrayList<>(count);

		Calendar calendar = pattern.nextMatchAfter(CalendarUtil.calendar(start));
		while(calendar.getTimeInMillis() < end){
			result.add(DateUtil.date(calendar));
			if(result.size() >= count){
				break;
			}
			calendar = pattern.nextMatchAfter(calendar);
		}

		return result;
	}
}
