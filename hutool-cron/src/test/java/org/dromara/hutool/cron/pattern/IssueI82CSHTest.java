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

package org.dromara.hutool.cron.pattern;

import org.dromara.hutool.core.date.DateTime;
import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class IssueI82CSHTest {

	@SuppressWarnings("DataFlowIssue")
	@Test
	void test() {
		final DateTime begin = DateUtil.parse("2023-09-20");
		final DateTime end = DateUtil.parse("2025-09-20");
		final List<Date> dates = CronPatternUtil.matchedDates("0 0 1 3-3,9 *", begin, end, 20);
		//dates.forEach(Console::log);
		Assertions.assertEquals(4,  dates.size());
	}
}
