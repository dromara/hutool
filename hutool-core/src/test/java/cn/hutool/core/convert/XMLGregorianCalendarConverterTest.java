package cn.hutool.core.convert;

import cn.hutool.core.date.DateUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;

public class XMLGregorianCalendarConverterTest {

	@Test
	public void convertTest(){
		final XMLGregorianCalendar calendar = Convert.convert(XMLGregorianCalendar.class, DateUtil.parse("2022-01-03 04:00:00"));
		Assert.assertNotNull(calendar);
	}
}
