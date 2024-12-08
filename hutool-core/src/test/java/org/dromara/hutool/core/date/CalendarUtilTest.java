/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.core.date;

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Objects;

public class CalendarUtilTest {

	@Test
	public void formatChineseDate(){
		final Calendar calendar = Objects.requireNonNull(DateUtil.parse("2018-02-24 12:13:14")).toCalendar();
		final String chineseDate = CalendarUtil.formatChineseDate(calendar, false);
		Assertions.assertEquals("二〇一八年二月二十四日", chineseDate);
		final String chineseDateTime = CalendarUtil.formatChineseDate(calendar, true);
		Assertions.assertEquals("二〇一八年二月二十四日十二时十三分十四秒", chineseDateTime);
	}

	@Test
	public void parseTest(){
		Assertions.assertThrows(IllegalArgumentException.class, ()->{
			final Calendar calendar = CalendarUtil.parse("2021-09-27 00:00:112323",
				DateFormatPool.NORM_DATETIME_FORMAT, false);

			// https://github.com/dromara/hutool/issues/1849
			// 在使用严格模式时，秒不正确，抛出异常
			DateUtil.date(calendar);
		});
	}

	@Test
	void setTimeZoneTest() {
		// Calendar中，设置时区并不会变化时间戳
		final Calendar instance = Calendar.getInstance();
		Console.log(instance.getTimeInMillis());
		final long timeInMillis = instance.getTimeInMillis();
		instance.setTimeZone(ZoneUtil.ZONE_UTC);
		final long timeInMillis2 = instance.getTimeInMillis();

		Assertions.assertEquals(timeInMillis, timeInMillis2);
	}
}
