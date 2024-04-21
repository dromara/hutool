/*
 * Copyright (c) 2024. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
