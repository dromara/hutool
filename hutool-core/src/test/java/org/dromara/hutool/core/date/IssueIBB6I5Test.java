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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * ISO8601时间中，不加`Z`表示一个本地时间，加`Z`表示UTC时间
 */
public class IssueIBB6I5Test {
	@Test
	void parseISO8601Test() {
		// 本测试中，解析一个UTC时间，当变更时区时，输出的时间就是对应的本地时间
		final DateTime date = DateUtil.parse("2024-12-13T08:02:27Z");
		final TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"));
		date.setTimeZone(timeZone);
		Assertions.assertEquals("2024-12-13 16:02:27", date.toString());
	}

	@Test
	void parseISO8601Test2() {
		// 本测试中，解析一个本地时间，当变更时区时，输出的时间不变
		final DateTime date = DateUtil.parse("2024-12-13T08:02:27");
		final TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"));
		date.setTimeZone(timeZone);
		Assertions.assertEquals("2024-12-13 08:02:27", date.toString());
	}
}
