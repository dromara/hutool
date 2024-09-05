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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateConvertTest {

	@Test
	public void toDateTest() {
		final String a = "2017-05-06";
		final Date value = ConvertUtil.toDate(a);
		Assertions.assertEquals(a, DateUtil.formatDate(value));

		final long timeLong = DateUtil.now().getTime();
		final Date value2 = ConvertUtil.toDate(timeLong);
		Assertions.assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toDateFromIntTest() {
		final int dateLong = -1497600000;
		final Date value = ConvertUtil.toDate(dateLong);
		Assertions.assertNotNull(value);
		Assertions.assertEquals("Mon Dec 15 00:00:00 CST 1969", value.toString().replace("GMT+08:00", "CST"));

		final java.sql.Date sqlDate = ConvertUtil.convert(java.sql.Date.class, dateLong);
		Assertions.assertNotNull(sqlDate);
		Assertions.assertEquals("1969-12-15", sqlDate.toString());
	}

	@Test
	public void toDateFromLocalDateTimeTest() {
		final LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		final Date value = ConvertUtil.toDate(localDateTime);
		Assertions.assertNotNull(value);
		Assertions.assertEquals("2017-05-06", DateUtil.formatDate(value));
	}

	@Test
	public void toSqlDateTest() {
		final String a = "2017-05-06";
		final java.sql.Date value = ConvertUtil.convert(java.sql.Date.class, a);
		Assertions.assertEquals("2017-05-06", value.toString());

		final long timeLong = DateUtil.now().getTime();
		final java.sql.Date value2 = ConvertUtil.convert(java.sql.Date.class, timeLong);
		Assertions.assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toLocalDateTimeTest() {
		final Date src = new Date();

		LocalDateTime ldt = ConvertUtil.toLocalDateTime(src);
		Assertions.assertEquals(ldt, DateUtil.toLocalDateTime(src));

		final Timestamp ts = Timestamp.from(src.toInstant());
		ldt = ConvertUtil.toLocalDateTime(ts);
		Assertions.assertEquals(ldt, DateUtil.toLocalDateTime(src));

		final String str = "2020-12-12 12:12:12.0";
		ldt = ConvertUtil.toLocalDateTime(str);
		Assertions.assertEquals(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")), str);
	}
}
