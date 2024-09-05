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

import java.util.Calendar;

public class MonthTest {

	@SuppressWarnings("ConstantConditions")
	@Test
	public void getLastDayTest(){
		int lastDay = Month.of(Calendar.JANUARY).getLastDay(false);
		Assertions.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.FEBRUARY).getLastDay(false);
		Assertions.assertEquals(28, lastDay);
		lastDay = Month.of(Calendar.FEBRUARY).getLastDay(true);
		Assertions.assertEquals(29, lastDay);
		lastDay = Month.of(Calendar.MARCH).getLastDay(true);
		Assertions.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.APRIL).getLastDay(true);
		Assertions.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.MAY).getLastDay(true);
		Assertions.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.JUNE).getLastDay(true);
		Assertions.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.JULY).getLastDay(true);
		Assertions.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.AUGUST).getLastDay(true);
		Assertions.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.SEPTEMBER).getLastDay(true);
		Assertions.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.OCTOBER).getLastDay(true);
		Assertions.assertEquals(31, lastDay);
		lastDay = Month.of(Calendar.NOVEMBER).getLastDay(true);
		Assertions.assertEquals(30, lastDay);
		lastDay = Month.of(Calendar.DECEMBER).getLastDay(true);
		Assertions.assertEquals(31, lastDay);
	}

	@Test
	public void toJdkMonthTest(){
		final java.time.Month month = Month.AUGUST.toJdkMonth();
		Assertions.assertEquals(java.time.Month.AUGUST, month);
	}

	@Test
	public void toJdkMonthTest2(){
		Assertions.assertThrows(IllegalArgumentException.class, Month.UNDECIMBER::toJdkMonth);
	}

	@Test
	public void ofTest(){
		Month month = Month.of("Jan");
		Assertions.assertEquals(Month.JANUARY, month);

		month = Month.of("JAN");
		Assertions.assertEquals(Month.JANUARY, month);

		month = Month.of("FEBRUARY");
		Assertions.assertEquals(Month.FEBRUARY, month);

		month = Month.of("February");
		Assertions.assertEquals(Month.FEBRUARY, month);

		month = Month.of(java.time.Month.FEBRUARY);
		Assertions.assertEquals(Month.FEBRUARY, month);

		month = Month.of("二月");
		Assertions.assertEquals(Month.FEBRUARY, month);
		month = Month.of("十月");
		Assertions.assertEquals(Month.OCTOBER, month);
		month = Month.of("十一月");
		Assertions.assertEquals(Month.NOVEMBER, month);
		month = Month.of("十二月");
		Assertions.assertEquals(Month.DECEMBER, month);
	}
}
