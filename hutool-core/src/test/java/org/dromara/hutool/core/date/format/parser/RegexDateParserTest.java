package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;

public class RegexDateParserTest {
	@Test
	void parsePureTest() {
		// yyyyMMdd
		final RegexDateParser parser = RegexDateParser.of("^(?<year>\\d{4})(?<month>\\d{2})(?<day>\\d{2})$");
		final Date parse = parser.parse("20220101");
		Assertions.assertEquals("2022-01-01", DateUtil.date(parse).toDateStr());
	}

	@Test
	void parseMonthFirstTest() {
		// May 8, 2009 5:57:51
		final RegexDateParser parser = RegexDateParser.of("(?<month>\\w+)\\W+(?<day>\\d{1,2})(?:th)?\\W+(?<year>\\d{2,4})(\\s(?<hour>\\d{1,2}):(?<minute>\\d{1,2}):(?<second>\\d{1,2}))?");

		final Date parse = parser.parse("May 8, 2009 5:57:51");
		Assertions.assertEquals("2009-05-08 05:57:51", DateUtil.date(parse).toString());

		final Date parse2 = parser.parse("May 8, 2009");
		Assertions.assertEquals("2009-05-08 00:00:00", DateUtil.date(parse2).toString());
	}
}
