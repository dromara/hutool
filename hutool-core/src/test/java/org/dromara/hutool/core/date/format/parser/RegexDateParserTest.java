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
		// May 8, 2009 5:57:51 PM
		final RegexDateParser parser = RegexDateParser.of("(?<month>\\w+)\\W+(?<day>\\d{1,2})(?:th)?\\W+(?<year>\\d{2,4}) ");
		final Date parse = parser.parse("May 8, 2009");
		Assertions.assertEquals("2009-05-08", DateUtil.date(parse).toDateStr());
	}
}
