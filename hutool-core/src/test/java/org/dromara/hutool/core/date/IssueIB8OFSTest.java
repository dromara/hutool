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

import org.junit.jupiter.api.Test;

import java.util.List;

public class IssueIB8OFSTest {
	@Test
	void rangeTest() {
		DateRange startRange = DateUtil.range(
			DateUtil.parse("2017-01-01"),
			DateUtil.parse("2017-01-31"), DateField.DAY_OF_YEAR);
		DateRange endRange = DateUtil.range(
			DateUtil.parse("2017-01-31"),
			DateUtil.parse("2017-02-02"), DateField.DAY_OF_YEAR);

		List<DateTime> dateTimes = DateUtil.rangeContains(startRange, endRange);
		System.out.println("交集: ");
		dateTimes.forEach(System.out::println);

		List<DateTime> dateNotTimes = DateUtil.rangeNotContains(startRange, endRange);
		System.out.println("差集: ");
		dateNotTimes.forEach(System.out::println);
	}
}
