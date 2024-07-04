package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * https://gitee.com/dromara/hutool/issues/I8IUTB
 */
public class DefaultRegexDateParserTest {

	@Test
	void parseYearMonthDaySplitByDashedTest() {
		assertParse("2013-02-03 00:00:00", "2013-Feb-03");
		assertParse("2013-02-03 00:00:00", "2013-02-03");
		assertParse("2013-02-03 00:00:00", "2013-2-03");
		assertParse("2013-02-03 00:00:00", "2013-2-3");
		assertParse("2014-04-26 00:00:00", "2014-04-26");
		assertParse("2014-04-01 00:00:00", "2014-04");

		assertParse("2013-04-01 22:43:00", "2013-04-01 22:43");
		assertParse("2013-04-01 22:43:22", "2013-04-01 22:43:22");
		assertParse("2014-04-26 17:24:37", "2014-04-26 17:24:37.3186369");
		assertParse("2012-08-03 18:31:59", "2012-08-03 18:31:59.257000000");
		assertParse("2014-04-26 17:24:37", "2014-04-26 17:24:37.123");
		assertParse("2014-12-16 14:20:00", "2014-12-16 06:20:00 UTC");
		assertParse("2014-12-16 14:20:00", "2014-12-16 06:20:00 GMT");
		assertParse("2014-04-26 17:24:37", "2014-04-26 05:24:37 PM");
		assertParse("2014-04-26 13:13:43", "2014-04-26 13:13:43 +0800");
		assertParse("2014-04-26 12:13:44", "2014-04-26 13:13:44 +09:00");
		assertParse("2014-04-26 13:13:43", "2014-04-26 13:13:43 +0800 +08");
		assertParse("2012-08-04 02:31:59", "2012-08-03 18:31:59.257000000 +0000 UTC");
		assertParse("2015-10-01 02:48:56", "2015-09-30 18:48:56.35272715 +0000 UTC");
		assertParse("2015-02-18 08:12:00", "2015-02-18 00:12:00 +0000 GMT");
		assertParse("2015-02-18 08:12:00", "2015-02-18 00:12:00 +0000 UTC");
		assertParse("2017-07-19 11:21:51", "2017-07-19 03:21:51+00:00");
		assertParse("2014-05-11 08:20:13", "2014-05-11 08:20:13,787");
	}

	@Test
	void parseYearMonthDaySplitByDashedWithMaskTest() {
		assertParse("2015-02-08 08:02:00", "2015-02-08 03:02:00 +0300 MSK m=+0.000000001");
		assertParse("2015-02-08 08:02:00", "2015-02-08 03:02:00.001 +0300 MSK m=+0.000000001");
	}

	@Test
	void parseYearMonthDaySplitBySlashTest() {
		assertParse("2014-03-31 00:00:00", "2014/3/31");
		assertParse("2014-03-31 00:00:00", "2014/03/31");
		assertParse("2014-04-08 22:05:00", "2014/4/8 22:05");
		assertParse("2014-04-08 22:05:00", "2014/04/08 22:05");
		assertParse("2014-04-02 03:00:51", "2014/04/2 03:00:51");
		assertParse("2014-04-02 03:00:51", "2014/4/02 03:00:51");
		assertParse("2012-03-19 10:11:59", "2012/03/19 10:11:59");
		assertParse("2012-03-19 10:11:59", "2012/03/19 10:11:59.3186369");
	}

	@Test
	void parseYearMonthDaySplitByChineseTest() {
		assertParse("2014-04-08 00:00:00", "2014年04月08日");
		assertParse("2017-02-01 12:23:45", "2017年02月01日 12时23分45秒");
		assertParse("2017-02-01 12:23:45", "2017年02月01日 12:23:45");
		assertParse("2024-03-28 22:33:49", "星期四, 28 三月 2024 14:33:49 GMT");
		assertParse("2024-03-28 22:33:49", "周四, 28 三月 2024 14:33:49 GMT");
	}

	@Test
	void parseYearMonthDaySplitByTTest() {
		assertParse("2006-01-02 23:04:05", "2006-01-02T15:04:05+0000");
		assertParse("2009-08-13 13:15:09", "2009-08-12T22:15:09-07:00");
		assertParse("2009-08-12 22:15:09", "2009-08-12T22:15:09+0800");
		assertParse("2009-08-12 22:15:09", "2009-08-12T22:15:09+08");
		assertParse("2009-08-12 22:15:09", "2009-08-12T22:15:09");
		assertParse("2009-08-13 06:15:09", "2009-08-12T22:15:09Z");// Z表示UTC时间
	}

	@Test
	void parseMonthDayYearTest() {
		assertParse("1970-10-07 00:00:00", "oct 7, 1970");
		assertParse("1970-10-07 00:00:00", "oct 7, '70");
		assertParse("1970-10-07 00:00:00", "oct. 7, 1970");
		assertParse("1970-10-07 00:00:00", "oct. 7, 70");
		assertParse("1970-10-07 00:00:00", "October 7, 1970");
		assertParse("1970-10-07 00:00:00", "October 7th, 1970");
		assertParse("1970-10-07 00:00:00", "October 7th, 1970");
		assertParse("1970-05-07 00:00:00", "May 7th, 1970");
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
		assertParse("2006-01-02 23:04:05", "Mon Jan 2 15:04:05 MST 2006");
		assertParse("2006-01-03 06:04:05", "Mon Jan 02 15:04:05 -0700 2006");
		assertParse("2015-08-10 22:44:11", "Mon Aug 10 15:44:11 UTC+0100 2015");
	}

	@Test
	void parseWeekDayMonthYearTimeTest() {
		assertParse("2006-01-02 23:04:05", "Monday, 02-Jan-06 15:04:05 MST");
		assertParse("2006-01-02 23:04:05", "Mon, 02 Jan 2006 15:04:05 MST");
		assertParse("2017-07-11 22:28:13", "Tue, 11 Jul 2017 16:28:13 +0200");
		assertParse("2017-07-11 22:28:13", "Tue, 11 Jul 2017 16:28:13 +0200 (CEST)");
		assertParse("2006-01-03 06:04:05", "Mon, 02 Jan 2006 15:04:05 -0700");
		assertParse("2018-01-05 01:53:36", "Thu, 4 Jan 2018 17:53:36 +0000");
	}

	@Test
	void parseDayMonthYearTest() {
		assertParse("2006-02-12 19:17:00", "12 Feb 2006, 19:17");
		assertParse("2006-02-12 19:17:00", "12 Feb 2006 19:17");
		assertParse("1970-10-07 00:00:00", "7 oct 70");
		assertParse("1970-10-07 00:00:00", "7 oct 1970");
		assertParse("2013-02-03 00:00:00", "03 February 2013");
		assertParse("2013-07-01 00:00:00", "1 July 2013");
	}

	@Test
	void parseDayOrMonthTest() {
		assertParse("2014-03-03 00:00:00", "3.3.2014");
		// 自动识别月在前
		assertParse("2014-03-31 00:00:00", "3/31/2014");
		assertParse("2014-03-31 00:00:00", "3.31.2014");
		assertParse("2014-03-31 00:00:00", "03/31/2014");
		assertParse("2014-03-31 00:00:00", "03.31.2014");
		assertParse("2014-08-04 22:05:00", "4/8/2014 22:05");
		assertParse("2014-08-04 22:05:00", "4/8/2014 22:05");
		assertParse("2014-08-04 22:05:00", "04/08/2014 22:05");
		assertParse("2014-02-04 03:00:51", "04/2/2014 03:00:51");
		assertParse("1965-08-08 00:00:00", "8/8/1965 12:00:00 AM");
		assertParse("1965-08-08 12:00:00", "8/8/1965 12:00:00 PM");
		assertParse("1965-08-08 13:00:00", "8/8/1965 01:00 PM");
		assertParse("1965-08-08 13:00:00", "8/8/1965 1:00 PM");
		assertParse("1965-08-08 00:00:00", "8/8/1965 12:00 AM");
		assertParse("2014-02-04 03:00:51", "4/02/2014 03:00:51");
		assertParse("2012-03-19 10:11:59", "03/19/2012 10:11:59");
		assertParse("2012-03-19 10:11:59", "03/19/2012 10:11:59.3186369");
	}

	@Test
	void parsePureNumberTest() {
		assertParse("2014-01-01 00:00:00", "2014");
		assertParse("2014-06-01 00:00:00", "201406");
		assertParse("2014-06-02 00:00:00", "20140602");
		assertParse("2014-07-22 10:52:03", "20140722105203");

		// unixtime(10)
		assertParse("2012-03-19 18:11:59", "1332151919");
		// millisecond(13)
		assertParse("2013-11-12 08:32:47", "1384216367189");
		// microsecond(16)
		assertParse("2013-11-12 08:32:47", "1384216367111222");
		// nanosecond(19)
		assertParse("2013-11-12 08:32:47", "1384216367111222333");
	}

	private static void assertParse(final String dateStr, final String dateStrToParse) {
		final Date date = DefaultRegexDateParser.INSTANCE.parse(dateStrToParse);
		assertEquals(dateStr, DateUtil.date(date).toString());
	}
}
