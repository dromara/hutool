package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class TemporalAccessorUtilTest {

	@Test
	public void formatLocalDateTest(){
		final String format = TemporalAccessorUtil.format(LocalDate.of(2020, 12, 7), DatePattern.NORM_DATETIME_PATTERN);
		Assertions.assertEquals("2020-12-07 00:00:00", format);
	}

	@Test
	public void formatLocalTimeTest(){
		final String today = TemporalAccessorUtil.format(LocalDate.now(), DatePattern.NORM_DATE_PATTERN);
		final String format = TemporalAccessorUtil.format(LocalTime.MIN, DatePattern.NORM_DATETIME_PATTERN);
		Assertions.assertEquals(today + " 00:00:00", format);
	}

	@Test
	public void formatCustomTest(){
		final String today = TemporalAccessorUtil.format(
				LocalDate.of(2021, 6, 26), "#sss");
		Assertions.assertEquals("1624636800", today);

		final String today2 = TemporalAccessorUtil.format(
				LocalDate.of(2021, 6, 26), "#SSS");
		Assertions.assertEquals("1624636800000", today2);
	}
}
