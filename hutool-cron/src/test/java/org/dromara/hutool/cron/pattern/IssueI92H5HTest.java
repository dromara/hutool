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
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

public class IssueI92H5HTest {
	@Test
	void nextMatchAfterTest() {
		// TODO 待解决

		// 匹配所有月，返回下一月
		final DateTime date = DateUtil.parse("2022-04-08 07:44:16");
		final CronPattern pattern = new CronPattern("0 0 0 L 2 ?");
		//noinspection ConstantConditions
		final Calendar calendar = pattern.nextMatchAfter(date.toCalendar());
		Console.log(DateUtil.date(calendar));
	}
}
