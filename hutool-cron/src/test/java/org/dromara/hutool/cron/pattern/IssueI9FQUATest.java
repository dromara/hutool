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
import org.dromara.hutool.core.date.StopWatch;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.quartz.CronExpression;

import java.text.ParseException;
import java.util.Calendar;

public class IssueI9FQUATest {

	@Test
	void nextDateAfterTest() {
		final String cron = "0/5 * * * * ?";
		final Calendar calendar = CronPattern.of(cron).nextMatchAfter(
			DateUtil.parse("2024-01-01 00:00:00").toCalendar());

		//Console.log(DateUtil.date(calendar));
		Assertions.assertEquals("2024-01-01 00:00:05", DateUtil.date(calendar).toString());
	}

	@Test
	@Disabled
	void createPatternBatchTest() throws ParseException {
		final String cron = "0/5 * * * * ?";
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("Hutool");
		CronPattern.of(cron);
		stopWatch.stop();

		stopWatch.start("Quartz");
		new CronExpression(cron);
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint());
	}

	@Test
	@Disabled
	void nextDateAfterBatchTest() throws ParseException {
		final String cron = "0/5 * * * * ?";
		final DateTime date = DateUtil.parse("2024-04-11 11:31:30");
		final Calendar calendar = date.toCalendar();
		final CronPattern cronPattern = CronPattern.of(cron);
		final CronExpression expression = new CronExpression(cron);


		final StopWatch stopWatch = new StopWatch();

		stopWatch.start("Hutool");
		cronPattern.nextMatchAfter(calendar);
		stopWatch.stop();

		stopWatch.start("Quartz");
		expression.getNextValidTimeAfter(date);
		stopWatch.stop();

		Console.log(stopWatch.prettyPrint());
	}
}
