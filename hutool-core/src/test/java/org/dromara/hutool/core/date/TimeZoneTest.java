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

import java.util.TimeZone;

import org.dromara.hutool.core.date.format.FastDateFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeZoneTest {

	@Test
	public void timeZoneConvertTest() {
		final DateTime dt = DateUtil.parse("2018-07-10 21:44:32", //
				FastDateFormat.getInstance(DatePattern.NORM_DATETIME_PATTERN, TimeZone.getTimeZone("GMT+8:00")));
		Assertions.assertEquals("2018-07-10 21:44:32", dt.toString());

		dt.setTimeZone(TimeZone.getTimeZone("Europe/London"));
		final int hour = dt.getField(DateField.HOUR_OF_DAY);
		Assertions.assertEquals(14, hour);
		Assertions.assertEquals("2018-07-10 14:44:32", dt.toString());
	}
}
