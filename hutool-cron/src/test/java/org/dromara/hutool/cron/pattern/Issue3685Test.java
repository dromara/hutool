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

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3685Test {
	@Test
	void nextDateAfterTest() {
		Date date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-01"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-02"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-03"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-04"));
		assertEquals("2024-08-05 00:00:00", date.toString());

		date = CronPatternUtil.nextDateAfter(CronPattern.of("0 0 * * MON"), DateUtil.parse("2024-08-05"));
		assertEquals("2024-08-12 00:00:00", date.toString());
	}
}
