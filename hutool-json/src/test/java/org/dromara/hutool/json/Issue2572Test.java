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

package org.dromara.hutool.json;

import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Month;
import java.time.MonthDay;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Issue2572Test {
	@Test
	public void putDayOfWeekTest(){
		final Set<DayOfWeek> weeks = new HashSet<>();
		weeks.add(DayOfWeek.MONDAY);
		final JSONObject obj = new JSONObject();
		obj.set("weeks", weeks);
		Assertions.assertEquals("{\"weeks\":[1]}", obj.toString());

		final Map<String, Set<DayOfWeek>> monthDays1 = obj.toBean(new TypeReference<Map<String, Set<DayOfWeek>>>() {
		});
		Assertions.assertEquals("{weeks=[MONDAY]}", monthDays1.toString());
	}

	@Test
	public void putMonthTest(){
		final Set<Month> months = new HashSet<>();
		months.add(Month.DECEMBER);
		final JSONObject obj = new JSONObject();
		obj.set("months", months);
		Assertions.assertEquals("{\"months\":[12]}", obj.toString());

		final Map<String, Set<Month>> monthDays1 = obj.toBean(new TypeReference<Map<String, Set<Month>>>() {
		});
		Assertions.assertEquals("{months=[DECEMBER]}", monthDays1.toString());
	}

	@Test
	public void putMonthDayTest(){
		final Set<MonthDay> monthDays = new HashSet<>();
		monthDays.add(MonthDay.of(Month.DECEMBER, 1));
		final JSONObject obj = new JSONObject();
		obj.set("monthDays", monthDays);
		Assertions.assertEquals("{\"monthDays\":[\"--12-01\"]}", obj.toString());

		final Map<String, Set<MonthDay>> monthDays1 = obj.toBean(new TypeReference<Map<String, Set<MonthDay>>>() {
		});
		Assertions.assertEquals("{monthDays=[--12-01]}", monthDays1.toString());
	}
}
