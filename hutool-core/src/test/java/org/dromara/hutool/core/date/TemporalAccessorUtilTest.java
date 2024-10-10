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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class TemporalAccessorUtilTest {

	@Test
	public void formatLocalDateTest(){
		final String format = TemporalAccessorUtil.format(LocalDate.of(2020, 12, 7), DateFormatPool.NORM_DATETIME_PATTERN);
		Assertions.assertEquals("2020-12-07 00:00:00", format);
	}

	@Test
	public void formatLocalTimeTest(){
		final String today = TemporalAccessorUtil.format(LocalDate.now(), DateFormatPool.NORM_DATE_PATTERN);
		final String format = TemporalAccessorUtil.format(LocalTime.MIN, DateFormatPool.NORM_DATETIME_PATTERN);
		Assertions.assertEquals(today + " 00:00:00", format);
	}

	@Test
	public void formatCustomTest(){
		final String today = TemporalAccessorUtil.format(
				LocalDate.of(2021, 6, 26), "#sss");
		Assertions.assertEquals("1624636800", today);

		final String today2 = TemporalAccessorUtil.format(
				LocalDate.of(2021, 6, 26), "#SSS");
		Assertions.assertEquals("1624636800000", today2);
	}

	@Test
	public void isInTest(){
		final String sourceStr = "2022-04-19 00:00:00";
		final String startTimeStr = "2022-04-19 00:00:00";
		final String endTimeStr = "2022-04-19 23:59:59";
		final boolean between = TimeUtil.isIn(
				TimeUtil.parse(sourceStr, DateFormatPool.NORM_DATETIME_FORMATTER),
				TimeUtil.parse(startTimeStr, DateFormatPool.NORM_DATETIME_FORMATTER),
				TimeUtil.parse(endTimeStr, DateFormatPool.NORM_DATETIME_FORMATTER));
		Assertions.assertTrue(between);
	}
}
