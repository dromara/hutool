package cn.hutool.core.convert;

import cn.hutool.core.date.DateUtil;
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
		final Date value = Convert.toDate(a);
		Assertions.assertEquals(a, DateUtil.formatDate(value));

		final long timeLong = DateUtil.now().getTime();
		final Date value2 = Convert.toDate(timeLong);
		Assertions.assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toDateFromIntTest() {
		final int dateLong = -1497600000;
		final Date value = Convert.toDate(dateLong);
		Assertions.assertNotNull(value);
		Assertions.assertEquals("Mon Dec 15 00:00:00 CST 1969", value.toString().replace("GMT+08:00", "CST"));

		final java.sql.Date sqlDate = Convert.convert(java.sql.Date.class, dateLong);
		Assertions.assertNotNull(sqlDate);
		Assertions.assertEquals("1969-12-15", sqlDate.toString());
	}

	@Test
	public void toDateFromLocalDateTimeTest() {
		final LocalDateTime localDateTime = LocalDateTime.parse("2017-05-06T08:30:00", DateTimeFormatter.ISO_DATE_TIME);
		final Date value = Convert.toDate(localDateTime);
		Assertions.assertNotNull(value);
		Assertions.assertEquals("2017-05-06", DateUtil.formatDate(value));
	}

	@Test
	public void toSqlDateTest() {
		final String a = "2017-05-06";
		final java.sql.Date value = Convert.convert(java.sql.Date.class, a);
		Assertions.assertEquals("2017-05-06", value.toString());

		final long timeLong = DateUtil.now().getTime();
		final java.sql.Date value2 = Convert.convert(java.sql.Date.class, timeLong);
		Assertions.assertEquals(timeLong, value2.getTime());
	}

	@Test
	public void toLocalDateTimeTest() {
		final Date src = new Date();

		LocalDateTime ldt = Convert.toLocalDateTime(src);
		Assertions.assertEquals(ldt, DateUtil.toLocalDateTime(src));

		final Timestamp ts = Timestamp.from(src.toInstant());
		ldt = Convert.toLocalDateTime(ts);
		Assertions.assertEquals(ldt, DateUtil.toLocalDateTime(src));

		final String str = "2020-12-12 12:12:12.0";
		ldt = Convert.toLocalDateTime(str);
		Assertions.assertEquals(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")), str);
	}
}
