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
		final List<Date> dates = CronPatternUtil.matchedDates("0 0 1 3-3,9 *", begin, end, 20, false);
		//dates.forEach(Console::log);
		Assertions.assertEquals(4,  dates.size());
	}
}
