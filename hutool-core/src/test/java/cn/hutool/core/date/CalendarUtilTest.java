package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;

public class CalendarUtilTest {

	@Test
	public void formatChineseDate(){
		Calendar calendar = DateUtil.parse("2018-02-24 12:13:14").toCalendar();
		final String chineseDate = CalendarUtil.formatChineseDate(calendar, false);
		Assert.assertEquals("二〇一八年二月二十四日", chineseDate);
		final String chineseDateTime = CalendarUtil.formatChineseDate(calendar, true);
		Assert.assertEquals("二〇一八年二月二十四日一十二时一十三分一十四秒", chineseDateTime);
	}
}
