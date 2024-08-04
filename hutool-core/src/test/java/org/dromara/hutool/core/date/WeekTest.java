/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
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

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WeekTest {

	@Test
	public void ofTest(){
		//测试别名及大小写
		assertEquals(Week.SUNDAY, Week.of("sun"));
		assertEquals(Week.SUNDAY, Week.of("SUN"));
		assertEquals(Week.SUNDAY, Week.of("Sun"));
		//测试全名及大小写
		assertEquals(Week.SUNDAY, Week.of("sunday"));
		assertEquals(Week.SUNDAY, Week.of("Sunday"));
		assertEquals(Week.SUNDAY, Week.of("SUNDAY"));

		assertEquals(Week.MONDAY, Week.of("Mon"));
		assertEquals(Week.MONDAY, Week.of("Monday"));

		assertEquals(Week.TUESDAY, Week.of("tue"));
		assertEquals(Week.TUESDAY, Week.of("tuesday"));

		assertEquals(Week.WEDNESDAY, Week.of("wed"));
		assertEquals(Week.WEDNESDAY, Week.of("WEDNESDAY"));

		assertEquals(Week.THURSDAY, Week.of("thu"));
		assertEquals(Week.THURSDAY, Week.of("THURSDAY"));

		assertEquals(Week.FRIDAY, Week.of("fri"));
		assertEquals(Week.FRIDAY, Week.of("FRIDAY"));

		assertEquals(Week.SATURDAY, Week.of("sat"));
		assertEquals(Week.SATURDAY, Week.of("SATURDAY"));
	}

	@Test
	public void ofTest2(){
		assertEquals(Week.SUNDAY, Week.of(DayOfWeek.SUNDAY));
		assertEquals(Week.MONDAY, Week.of(DayOfWeek.MONDAY));
		assertEquals(Week.TUESDAY, Week.of(DayOfWeek.TUESDAY));
		assertEquals(Week.WEDNESDAY, Week.of(DayOfWeek.WEDNESDAY));
		assertEquals(Week.THURSDAY, Week.of(DayOfWeek.THURSDAY));
		assertEquals(Week.FRIDAY, Week.of(DayOfWeek.FRIDAY));
		assertEquals(Week.SATURDAY, Week.of(DayOfWeek.SATURDAY));
		assertEquals(Week.SATURDAY, Week.of(Calendar.SATURDAY));
		assertNull(Week.of(10));
		assertNull(Week.of(-1));
	}

	@Test
	public void ofChineseTest() {
		assertEquals(Week.SUNDAY, Week.of("星期日"));
		assertEquals(Week.SUNDAY, Week.of("周日"));

		assertEquals(Week.MONDAY, Week.of("星期一"));
		assertEquals(Week.MONDAY, Week.of("周一"));

		assertEquals(Week.TUESDAY, Week.of("星期二"));
		assertEquals(Week.TUESDAY, Week.of("周二"));

		assertEquals(Week.WEDNESDAY, Week.of("星期三"));
		assertEquals(Week.WEDNESDAY, Week.of("周三"));

		assertEquals(Week.THURSDAY, Week.of("星期四"));
		assertEquals(Week.THURSDAY, Week.of("周四"));

		assertEquals(Week.FRIDAY, Week.of("星期五"));
		assertEquals(Week.FRIDAY, Week.of("周五"));

		assertEquals(Week.SATURDAY, Week.of("星期六"));
		assertEquals(Week.SATURDAY, Week.of("周六"));
	}

	@Test
	public void toJdkDayOfWeekTest(){
		assertEquals(DayOfWeek.MONDAY, Week.MONDAY.toJdkDayOfWeek());
		assertEquals(DayOfWeek.TUESDAY, Week.TUESDAY.toJdkDayOfWeek());
		assertEquals(DayOfWeek.WEDNESDAY, Week.WEDNESDAY.toJdkDayOfWeek());
		assertEquals(DayOfWeek.THURSDAY, Week.THURSDAY.toJdkDayOfWeek());
		assertEquals(DayOfWeek.FRIDAY, Week.FRIDAY.toJdkDayOfWeek());
		assertEquals(DayOfWeek.SATURDAY, Week.SATURDAY.toJdkDayOfWeek());
		assertEquals(DayOfWeek.SUNDAY, Week.SUNDAY.toJdkDayOfWeek());
	}

	@Test
	public void toChineseTest(){
		assertEquals("周一",Week.MONDAY.toChinese("周"));
		assertEquals("星期一",Week.MONDAY.toChinese("星期"));
		assertEquals("星期二",Week.TUESDAY.toChinese("星期"));
		assertEquals("星期三",Week.WEDNESDAY.toChinese("星期"));
		assertEquals("星期四",Week.THURSDAY.toChinese("星期"));
		assertEquals("星期五",Week.FRIDAY.toChinese("星期"));
		assertEquals("星期六",Week.SATURDAY.toChinese("星期"));
		assertEquals("星期日",Week.SUNDAY.toChinese("星期"));
		assertEquals("星期一",Week.MONDAY.toChinese());
	}
}
