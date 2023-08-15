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

package org.dromara.hutool.core.data;

import org.dromara.hutool.core.date.DateField;
import org.dromara.hutool.core.date.DateRange;
import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Issue3081Test {

	@Test
	void dateRangeTest() {
		final DateTime start = DateUtil.parse("2023-01-01 00:00:00");
		final DateTime end = DateUtil.parse("2023-04-25 00:00:00");

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
