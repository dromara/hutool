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
