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

import java.util.Calendar;

public class IssueI92H5HTest {
	@Test
	void nextMatchAfterTest() {
		// 匹配所有月，返回下一月
		final DateTime date = DateUtil.parse("2022-04-08 07:44:16");
		final CronPattern pattern = new CronPattern("0 0 0 L 2 ?");
		//noinspection ConstantConditions
		final Calendar calendar = pattern.nextMatchAfter(date.toCalendar());
		Assertions.assertEquals("2023-02-28 00:00:00", DateUtil.date(calendar).toString());
	}
}
