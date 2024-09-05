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

import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

public class DateBuilderTest {
	@Test
	public void testNormal() {
		final DateBuilder builder = new DateBuilder();
		builder.setYear(2019);
		builder.setMonth(10);
		builder.setDay(1);
		final Date date = builder.toDate();

		Assertions.assertEquals("2019-10-01 00:00:00", date.toString());
	}

	@Test
	public void testLocalDateTime() {
		final DateBuilder builder = DateBuilder.of()
			.setYear(2019)
			.setMonth(10)
			.setDay(1)
			.setHour(10)
			.setMinute(20)
			.setSecond(30)
			.setNs(900000000)
			.setZone(TimeZone.getDefault());

		final LocalDateTime dateTime = builder.toLocalDateTime();
		Assertions.assertEquals("2019-10-01T10:20:30.900", dateTime.toString());
	}

	@Test
	public void testTimestamp() {
		final String timestamp = "946656000";
		final DateBuilder dateBuilder = DateBuilder.of().setUnixsecond(Long.parseLong(timestamp));
		Assertions.assertEquals("2000-01-01T00:00", dateBuilder.toLocalDateTime().toString());
	}
}
