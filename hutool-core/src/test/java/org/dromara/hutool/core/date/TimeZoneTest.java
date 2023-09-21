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

import java.util.TimeZone;

import org.dromara.hutool.core.date.format.FastDateFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeZoneTest {

	@Test
	public void timeZoneConvertTest() {
		final DateTime dt = DateUtil.parse("2018-07-10 21:44:32", //
				FastDateFormat.getInstance(DatePattern.NORM_DATETIME_PATTERN, TimeZone.getTimeZone("GMT+8:00")));
		Assertions.assertEquals("2018-07-10 21:44:32", dt.toString());

		dt.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		final int hour = dt.getField(DateField.HOUR_OF_DAY);
		Assertions.assertEquals(14, hour);
		Assertions.assertEquals("2018-07-10 14:44:32", dt.toString());
	}
}
