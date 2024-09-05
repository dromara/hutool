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

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.date.DateField;
import org.dromara.hutool.core.date.DateRange;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Issue3081Test {

	@Test
	void dateRangeTest() {
		final Date start = DateUtil.parse("2023-01-01 00:00:00");
		final Date end = DateUtil.parse("2023-04-25 00:00:00");

		final DateRange dateTimes = new DateRange(start, end, DateField.DAY_OF_MONTH, 30, true, true);

		final List<DateTime> dateTimeList = new ArrayList<>();
		while (dateTimes.hasNext()) {
			dateTimeList.add(dateTimes.next());
		}
		Assertions.assertEquals(4, dateTimeList.size());

		Assertions.assertEquals(DateUtil.parse("2023-01-01 00:00:00"), dateTimeList.get(0));
		Assertions.assertEquals(DateUtil.parse("2023-01-31 00:00:00"), dateTimeList.get(1));
		Assertions.assertEquals(DateUtil.parse("2023-03-02 00:00:00"), dateTimeList.get(2));
		Assertions.assertEquals(DateUtil.parse("2023-04-01 00:00:00"), dateTimeList.get(3));
	}
}
