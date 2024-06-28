package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://gitee.com/dromara/hutool/issues/I8IUTB
 */
public class GlobalRegexDateParserTest {

	@Test
	void parseMonthDayYearTest() {
		assertParse("1970-10-07 00:00:00", "oct 7, 1970");
		assertParse("1970-10-07 00:00:00", "oct 7, '70");
		assertParse("1970-10-07 00:00:00", "oct. 7, 1970");
		assertParse("1970-10-07 00:00:00", "oct. 7, 70");
		assertParse("1970-10-07 00:00:00", "October 7, 1970");
		assertParse("1970-10-07 00:00:00", "October 7th, 1970");
		assertParse("1970-10-07 00:00:00", "October 7th, 1970");
	}

	@Test
	void parseMonthDayYearTimeTest() {
		assertParse("2009-05-08 05:57:51", "May 8, 2009 5:57:51");
		assertParse("2009-05-08 17:57:51", "May 8, 2009 5:57:51 PM");
		assertParse("2009-05-08 17:57:51", "May 8, 2009 5:57:51 pm");
		assertParse("2009-05-08 17:57:51", "May 8, 2009 5:57:51pm");
		assertParse("2009-05-08 05:57:51", "May 8, 2009 5:57:51 +08:00");
		assertParse("2009-05-08 05:57:51", "May 8, 2009 5:57:51 +0800");
		assertParse("2009-05-08 05:57:51", "May 8, 2009 5:57:51 +08");
		assertParse("2009-05-08 00:00:00", "May 8, 2009");
		assertParse("2009-05-08 00:00:00", "May 8th, 2009");
		assertParse("2009-05-08 00:00:00", "May 8th, 09");
		assertParse("2009-05-08 00:00:00", "may. 8th, 09");
		assertParse("2012-09-17 10:09:00", "September 17, 2012 10:09am");
		assertParse("2012-09-18 02:09:00", "September 17, 2012 at 10:09am PST-08");
		assertParse("2012-09-17 10:10:09", "September 17, 2012, 10:10:09");
	}

	@Test
	void parseWeekMonthDayYearTimeTest() {
		assertParse("2006-01-02 15:04:05", "Mon Jan 2, 2006 15:04:05");
		assertParse("2006-01-02 15:04:05", "Mon Jan 2 2006 15:04:05");
		assertParse("2015-07-04 01:04:07", "Fri Jul 03 2015 18:04:07 GMT+0100 (GMT Daylight Time)");
	}

	@Test
	void parseWeekMonthDayTimeYearTest() {
		assertParse("2006-01-02 15:04:05", "Mon Jan 2 15:04:05 2006");
		assertParse("2006-01-02 15:04:05", "Mon Jan 2 15:04:05 MST 2006");
		assertParse("2006-01-03 06:04:05", "Mon Jan 02 15:04:05 -0700 2006");
		assertParse("2015-08-10 22:44:11", "Mon Aug 10 15:44:11 UTC+0100 2015");
	}

	@Test
	void parseWeekDayMonthYearTimeTest() {
		assertParse("2006-01-02 15:04:05", "Monday, 02-Jan-06 15:04:05 MST");
		assertParse("2006-01-02 15:04:05", "Mon, 02 Jan 2006 15:04:05 MST");
		assertParse("2017-07-11 22:28:13", "Tue, 11 Jul 2017 16:28:13 +0200");
		assertParse("2017-07-11 22:28:13", "Tue, 11 Jul 2017 16:28:13 +0200 (CEST)");
		assertParse("2006-01-03 06:04:05", "Mon, 02 Jan 2006 15:04:05 -0700");
		assertParse("2018-01-05 01:53:36", "Thu, 4 Jan 2018 17:53:36 +0000");
	}

	@Test
	void parseDayMonthYear() {
		assertParse("2006-02-12 19:17:00", "12 Feb 2006, 19:17");
		assertParse("2006-02-12 19:17:00", "12 Feb 2006 19:17");
		assertParse("1970-10-07 00:00:00", "7 oct 70");
		assertParse("1970-10-07 00:00:00", "7 oct 1970");
		assertParse("2013-02-03 00:00:00", "03 February 2013");
		assertParse("2013-07-01 00:00:00", "1 July 2013");
	}

	private static void assertParse(final String dateStr, final String dateStrToParse) {
		final Date date = GlobalRegexDateParser.parse(dateStrToParse);
		assertEquals(dateStr, DateUtil.date(date).toString());
	}
}
