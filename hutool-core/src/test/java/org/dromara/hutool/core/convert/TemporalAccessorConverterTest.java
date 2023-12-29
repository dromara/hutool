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
		final Instant instant = Convert.convert(Instant.class, dateStr);
		final Instant instant1 = Objects.requireNonNull(DateUtil.parse(dateStr)).toInstant();
		Assertions.assertEquals(instant1, instant);
	}

	@Test
	public void toLocalDateTimeTest(){
		final LocalDateTime localDateTime = Convert.convert(LocalDateTime.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18T00:00", localDateTime.toString());
	}

	@Test
	public void toLocalDateTest(){
		final LocalDate localDate = Convert.convert(LocalDate.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18", localDate.toString());
	}

	@Test
	public void toLocalTimeTest(){
		final LocalTime localTime = Convert.convert(LocalTime.class, "2019-02-18");
		Assertions.assertEquals("00:00", localTime.toString());
	}

	@Test
	public void toZonedDateTimeTest(){
		final ZonedDateTime zonedDateTime = Convert.convert(ZonedDateTime.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString().substring(0, 22));
	}

	@Test
	public void toOffsetDateTimeTest(){
		final OffsetDateTime zonedDateTime = Convert.convert(OffsetDateTime.class, "2019-02-18");
		Assertions.assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString());
	}

	@Test
	public void toOffsetTimeTest(){
		final OffsetTime offsetTime = Convert.convert(OffsetTime.class, "2019-02-18");
		Assertions.assertEquals("00:00+08:00", offsetTime.toString());
	}
}
