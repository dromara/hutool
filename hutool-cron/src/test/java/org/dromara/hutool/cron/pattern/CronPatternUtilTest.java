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

package org.dromara.hutool.cron.pattern;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class CronPatternUtilTest {

	@Test
	public void matchedDatesTest() {
		//测试每30秒执行
		final List<Date> matchedDates = CronPatternUtil.matchedDates("0/30 * 8-18 * * ?", DateUtil.parse("2018-10-15 14:33:22"), 5, true);
		Assertions.assertEquals(5, matchedDates.size());
		Assertions.assertEquals("2018-10-15 14:33:30", matchedDates.get(0).toString());
		Assertions.assertEquals("2018-10-15 14:34:00", matchedDates.get(1).toString());
		Assertions.assertEquals("2018-10-15 14:34:30", matchedDates.get(2).toString());
		Assertions.assertEquals("2018-10-15 14:35:00", matchedDates.get(3).toString());
		Assertions.assertEquals("2018-10-15 14:35:30", matchedDates.get(4).toString());
	}

	@Test
	public void matchedDatesTest2() {
		//测试每小时执行
		final List<Date> matchedDates = CronPatternUtil.matchedDates("0 0 */1 * * *", DateUtil.parse("2018-10-15 14:33:22"), 5, true);
		Assertions.assertEquals(5, matchedDates.size());
		Assertions.assertEquals("2018-10-15 15:00:00", matchedDates.get(0).toString());
		Assertions.assertEquals("2018-10-15 16:00:00", matchedDates.get(1).toString());
		Assertions.assertEquals("2018-10-15 17:00:00", matchedDates.get(2).toString());
		Assertions.assertEquals("2018-10-15 18:00:00", matchedDates.get(3).toString());
		Assertions.assertEquals("2018-10-15 19:00:00", matchedDates.get(4).toString());
	}

	@Test
	public void matchedDatesTest3() {
		//测试最后一天
		final List<Date> matchedDates = CronPatternUtil.matchedDates("0 0 */1 L * *", DateUtil.parse("2018-10-30 23:33:22"), 5, true);
		Assertions.assertEquals(5, matchedDates.size());
		Assertions.assertEquals("2018-10-31 00:00:00", matchedDates.get(0).toString());
		Assertions.assertEquals("2018-10-31 01:00:00", matchedDates.get(1).toString());
		Assertions.assertEquals("2018-10-31 02:00:00", matchedDates.get(2).toString());
		Assertions.assertEquals("2018-10-31 03:00:00", matchedDates.get(3).toString());
		Assertions.assertEquals("2018-10-31 04:00:00", matchedDates.get(4).toString());
	}
}
