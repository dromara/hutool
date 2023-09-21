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

package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class DateModifierTest {

	@Test
	public void truncateTest() {
		final String dateStr = "2017-03-01 22:33:23.123";
		final Date date = DateUtil.parse(dateStr);

		// 毫秒
		DateTime begin = DateUtil.truncate(date, DateField.MILLISECOND);
		Assertions.assertEquals(dateStr, begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 秒
		begin = DateUtil.truncate(date, DateField.SECOND);
		Assertions.assertEquals("2017-03-01 22:33:23.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 分
		begin = DateUtil.truncate(date, DateField.MINUTE);
		Assertions.assertEquals("2017-03-01 22:33:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 小时
		begin = DateUtil.truncate(date, DateField.HOUR);
		Assertions.assertEquals("2017-03-01 22:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.HOUR_OF_DAY);
		Assertions.assertEquals("2017-03-01 22:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 上下午，原始日期是22点，上下午的起始就是12点
		begin = DateUtil.truncate(date, DateField.AM_PM);
		Assertions.assertEquals("2017-03-01 12:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 天，day of xxx按照day处理
		begin = DateUtil.truncate(date, DateField.DAY_OF_WEEK_IN_MONTH);
		Assertions.assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.DAY_OF_WEEK);
		Assertions.assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.DAY_OF_MONTH);
		Assertions.assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 星期
		begin = DateUtil.truncate(date, DateField.WEEK_OF_MONTH);
		Assertions.assertEquals("2017-02-27 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.truncate(date, DateField.WEEK_OF_YEAR);
		Assertions.assertEquals("2017-02-27 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 月
		begin = DateUtil.truncate(date, DateField.MONTH);
		Assertions.assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 年
		begin = DateUtil.truncate(date, DateField.YEAR);
		Assertions.assertEquals("2017-01-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	@Test
	public void truncateDayOfWeekInMonthTest() {
		final String dateStr = "2017-03-01 22:33:23.123";
		final Date date = DateUtil.parse(dateStr);

		// 天，day of xxx按照day处理
		final DateTime begin = DateUtil.truncate(date, DateField.DAY_OF_WEEK_IN_MONTH);
		Assertions.assertEquals("2017-03-01 00:00:00.000", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	@Test
	public void ceilingTest() {
		final String dateStr = "2017-03-01 22:33:23.123";
		final Date date = DateUtil.parse(dateStr);

		// 毫秒
		DateTime begin = DateUtil.ceiling(date, DateField.MILLISECOND);
		Assertions.assertEquals(dateStr, begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 秒
		begin = DateUtil.ceiling(date, DateField.SECOND);
		Assertions.assertEquals("2017-03-01 22:33:23.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 分
		begin = DateUtil.ceiling(date, DateField.MINUTE);
		Assertions.assertEquals("2017-03-01 22:33:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 小时
		begin = DateUtil.ceiling(date, DateField.HOUR);
		Assertions.assertEquals("2017-03-01 22:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.HOUR_OF_DAY);
		Assertions.assertEquals("2017-03-01 22:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 上下午，原始日期是22点，上下午的结束就是23点
		begin = DateUtil.ceiling(date, DateField.AM_PM);
		Assertions.assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 天，day of xxx按照day处理
		begin = DateUtil.ceiling(date, DateField.DAY_OF_WEEK_IN_MONTH);
		Assertions.assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.DAY_OF_WEEK);
		Assertions.assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.DAY_OF_MONTH);
		Assertions.assertEquals("2017-03-01 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 星期
		begin = DateUtil.ceiling(date, DateField.WEEK_OF_MONTH);
		Assertions.assertEquals("2017-03-05 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
		begin = DateUtil.ceiling(date, DateField.WEEK_OF_YEAR);
		Assertions.assertEquals("2017-03-05 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 月
		begin = DateUtil.ceiling(date, DateField.MONTH);
		Assertions.assertEquals("2017-03-31 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));

		// 年
		begin = DateUtil.ceiling(date, DateField.YEAR);
		Assertions.assertEquals("2017-12-31 23:59:59.999", begin.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}

	@Test
	public void roundTest(){
		// issues#I5M2I0
		final String dateStr = "2022-08-12 14:59:21.500";
		final Date date = DateUtil.parse(dateStr);

		final DateTime dateTime = DateUtil.round(date, DateField.SECOND);
		Assertions.assertEquals("2022-08-12 14:59:21.999", dateTime.toString(DatePattern.NORM_DATETIME_MS_PATTERN));
	}
}
