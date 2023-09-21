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
	}
}
