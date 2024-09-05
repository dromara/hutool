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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Objects;

public class TemporalAccessorConverterTest {

	@Test
	public void toInstantTest(){
		final String dateStr = "2019-02-18";

		// 通过转换获取的Instant为UTC时间
		final Instant instant = ConvertUtil.convert(Instant.class, dateStr);
		final Instant instant1 = Objects.requireNonNull(DateUtil.parse(dateStr)).toInstant();
		Assertions.assertEquals(instant1, instant);
	}

	@Test
	public void toLocalDateTimeTest(){
		final LocalDateTime localDateTime = ConvertUtil.convert(LocalDateTime.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18T00:00", localDateTime.toString());
	}

	@Test
	public void toLocalDateTest(){
		final LocalDate localDate = ConvertUtil.convert(LocalDate.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18", localDate.toString());
	}

	@Test
	public void toLocalTimeTest(){
		final LocalTime localTime = ConvertUtil.convert(LocalTime.class, "2019-02-18");
		Assertions.assertEquals("00:00", localTime.toString());
	}

	@Test
	public void toZonedDateTimeTest(){
		final ZonedDateTime zonedDateTime = ConvertUtil.convert(ZonedDateTime.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString().substring(0, 22));
	}

	@Test
	public void toOffsetDateTimeTest(){
		final OffsetDateTime zonedDateTime = ConvertUtil.convert(OffsetDateTime.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString());
	}

	@Test
	public void toOffsetTimeTest(){
		final OffsetTime offsetTime = ConvertUtil.convert(OffsetTime.class, "2019-02-18");
		Assertions.assertEquals("00:00+08:00", offsetTime.toString());
	}
}
