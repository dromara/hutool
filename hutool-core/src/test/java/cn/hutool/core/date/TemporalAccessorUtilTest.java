package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class TemporalAccessorUtilTest {

	@Test
	public void formatLocalDateTest(){
		final String format = TemporalAccessorUtil.format(LocalDate.of(2020, 12, 7), DatePattern.NORM_DATETIME_PATTERN);
		Assert.assertEquals("2020-12-07 00:00:00", format);
	}

	@Test
	public void formatLocalTimeTest(){
		final String today = TemporalAccessorUtil.format(LocalDate.now(), DatePattern.NORM_DATE_PATTERN);
		final String format = TemporalAccessorUtil.format(LocalTime.MIN, DatePattern.NORM_DATETIME_PATTERN);
		Assert.assertEquals(today + " 00:00:00", format);
	}
}
