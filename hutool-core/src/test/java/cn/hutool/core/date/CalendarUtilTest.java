package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Objects;

public class CalendarUtilTest {

	@Test
	public void formatChineseDate(){
		Calendar calendar = Objects.requireNonNull(DateUtil.parse("2018-02-24 12:13:14")).toCalendar();
		final String chineseDate = CalendarUtil.formatChineseDate(calendar, false);
		Assert.assertEquals("二〇一八年二月二十四日", chineseDate);
		final String chineseDateTime = CalendarUtil.formatChineseDate(calendar, true);
		Assert.assertEquals("二〇一八年二月二十四日十二时十三分十四秒", chineseDateTime);
	}

	@Test(expected = IllegalArgumentException.class)
	public void parseTest(){
		final Calendar calendar = CalendarUtil.parse("2021-09-27 00:00:112323", false,
				DatePattern.NORM_DATETIME_FORMAT);

		// https://github.com/dromara/hutool/issues/1849
		// 在使用严格模式时，秒不正确，抛出异常
		DateUtil.date(calendar);
	}
}
