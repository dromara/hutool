package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalRegexDateParserTest {

	@Test
	void parseMonthFirstTest() {
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
	}

	@Test
	void parseWeekFirstTest() {
		assertParse("2006-01-02 15:04:05", "Mon Jan 2, 2006 15:04:05");
		assertParse("2006-01-02 15:04:05", "Mon Jan 2 2006 15:04:05");
	}

	@Test
	void parseWeekFirstYearLastTest() {
		assertParse("2006-01-02 15:04:05", "Mon Jan 2 15:04:05 2006");
		assertParse("2006-01-02 15:04:05", "Mon Jan 2 15:04:05 MST 2006");
		assertParse("2006-01-03 06:04:05", "Mon Jan 02 15:04:05 -0700 2006");
		//assertParse("2006-01-03 06:04:05", "Monday, 02-Jan-06 15:04:05 MST");
	}

	private static void assertParse(final String dateStr, final String dateStrToParse) {
		final Date date = GlobalRegexDateParser.parse(dateStrToParse);
		assertEquals(dateStr, DateUtil.date(date).toString());
	}
}
