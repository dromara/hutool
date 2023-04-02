package org.dromara.hutool.convert;

import org.dromara.hutool.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.xml.datatype.XMLGregorianCalendar;

public class XMLGregorianCalendarConverterTest {

	@Test
	public void convertTest(){
		final XMLGregorianCalendar calendar = Convert.convert(XMLGregorianCalendar.class, DateUtil.parse("2022-01-03 04:00:00"));
		Assertions.assertNotNull(calendar);
	}
}
