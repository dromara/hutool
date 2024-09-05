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

public class Issue3011Test {
	@Test
	public void isSameMonthTest() {
		// https://github.com/dromara/hutool/issues/3011
		// 判断是否同一个月，还需考虑公元前和公元后的的情况
		// 此处公元前2020年和公元2021年返回年都是2021
		final Calendar calendar1 = Calendar.getInstance();
		calendar1.set(-2020, Calendar.FEBRUARY, 12);

		final Calendar calendar2 = Calendar.getInstance();
		calendar2.set(2021, Calendar.FEBRUARY, 12);


		Assertions.assertFalse(CalendarUtil.isSameMonth(calendar1, calendar2));
	}
}
