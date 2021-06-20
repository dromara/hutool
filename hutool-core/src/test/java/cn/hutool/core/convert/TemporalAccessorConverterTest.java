package cn.hutool.core.convert;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;

public class TemporalAccessorConverterTest {

	@Test
	public void toInstantTest(){
		String dateStr = "2019-02-18";

		// 通过转换获取的Instant为UTC时间
		Instant instant = Convert.convert(Instant.class, dateStr);
		Instant instant1 = DateUtil.parse(dateStr).toInstant();
		Assert.assertEquals(instant1, instant);
	}

	@Test
	public void toLocalDateTimeTest(){
		LocalDateTime localDateTime = Convert.convert(LocalDateTime.class, "2019-02-18");
		Assert.assertEquals("2019-02-18T00:00", localDateTime.toString());
	}

	@Test
	public void toLocalDateTest(){
		LocalDate localDate = Convert.convert(LocalDate.class, "2019-02-18");
		Assert.assertEquals("2019-02-18", localDate.toString());
	}

	@Test
	public void toLocalTimeTest(){
		LocalTime localTime = Convert.convert(LocalTime.class, "2019-02-18");
		Assert.assertEquals("00:00", localTime.toString());
	}

	@Test
	public void toZonedDateTimeTest(){
		ZonedDateTime zonedDateTime = Convert.convert(ZonedDateTime.class, "2019-02-18");
		Assert.assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString().substring(0, 22));
	}

	@Test
	public void toOffsetDateTimeTest(){
		OffsetDateTime zonedDateTime = Convert.convert(OffsetDateTime.class, "2019-02-18");
		Assert.assertEquals("2019-02-18T00:00+08:00", zonedDateTime.toString());
	}

	@Test
	public void toOffsetTimeTest(){
		OffsetTime offsetTime = Convert.convert(OffsetTime.class, "2019-02-18");
		Assert.assertEquals("00:00+08:00", offsetTime.toString());
	}
}
