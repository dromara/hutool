package org.dromara.hutool.core.date.format.parser;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class RegexDateParserTest {

	// hh:mm:ss.SSSSZ hh:mm:ss.SSSS hh:mm:ss hh:mm
	final String timeRegex = "(" +
		"\\s(?<hour>\\d{1,2})" +
		":(?<minute>\\d{1,2})" +
		"(:(?<second>\\d{1,2}))?" +
		"(?:[.,](?<ns>\\d{1,9}))?(?<zero>z)?" +
		"(\\s?(?<m>am|pm))?" +
		")?";
	// +08:00 +0800 +08
	final String zoneOffsetRegex = "(\\s?(?<zoneOffset>[-+]\\d{1,2}:?(?:\\d{2})?))?";
	// CST UTC (CST)
	final String zoneNameRegex = "(\\s[(]?(?<zoneName>[a-z ]+)[)]?)?";

	@Test
	void timeMatchTest() {
		assertMatch(timeRegex, " 15:04:05");
		assertMatch(timeRegex, " 15:04:05.3186369");
		assertMatch(timeRegex, " 15:04:05.3186369z");
		assertMatch(timeRegex, " 15:04:05.318");
		assertMatch(timeRegex, " 15:04:05.318");
		assertMatch(timeRegex, " 15:04");
		assertMatch(timeRegex, " 05:04pm");
		assertMatch(timeRegex, " 05:04 PM");
		assertMatch(timeRegex, " 05:04:12PM");
		assertMatch(timeRegex, " 05:04:12 PM");
		assertMatch(timeRegex, " 5:4pm");
		assertMatch(timeRegex, " 5:4am");
	}

	@Test
	void zoneOffsetMatchTest() {
		assertMatch(zoneOffsetRegex, "+0800");
		assertMatch(zoneOffsetRegex, "+08");
		assertMatch(zoneOffsetRegex, "+08:00");
		assertMatch(zoneOffsetRegex, " +08:00");
		assertMatch(zoneOffsetRegex, "+0000");
		assertMatch(zoneOffsetRegex, " -0700");
	}

	@Test
	void zoneNameMatchTest() {
		assertMatch(zoneNameRegex, " (CST)");
		assertMatch(zoneNameRegex, " CST");
		assertMatch(zoneNameRegex, " (GMT)");
		assertMatch(zoneNameRegex, " (CEST)");
		assertMatch(zoneNameRegex, " (GMT Daylight Time)");
	}

	@Test
	void parsePureTest() {
		// yyyyMMdd
		final RegexDateParser parser = RegexDateParser.of("^(?<year>\\d{4})(?<month>\\d{2})(?<day>\\d{2})$");
		final Date parse = parser.parse("20220101");
		assertEquals("2022-01-01", DateUtil.date(parse).toDateStr());
	}

	@Test
	void parseMonthFirstTest() {
		final String dateRegex = "(?<month>\\w+{3,9})\\W+(?<day>\\d{1,2})(?:th)?\\W+(?<year>\\d{2,4})";

		// May 8, 2009 5:57:51
		final RegexDateParser parser = RegexDateParser.of(dateRegex + timeRegex + zoneOffsetRegex);

		Date parse = parser.parse("May 8, 2009 5:57:51");
		assertEquals("2009-05-08 05:57:51", DateUtil.date(parse).toString());

		parse = parser.parse("May 8, 2009 5:57:51 +08:00");
		assertEquals("2009-05-08 05:57:51", DateUtil.date(parse).toString());

		parse = parser.parse("May 8, 2009 5:57:51 +0800");
		assertEquals("2009-05-08 05:57:51", DateUtil.date(parse).toString());

		parse = parser.parse("May 8, 2009 5:57:51 +08");
		assertEquals("2009-05-08 05:57:51", DateUtil.date(parse).toString());

		parse = parser.parse("May 8, 2009");
		assertEquals("2009-05-08 00:00:00", DateUtil.date(parse).toString());

		parse = parser.parse("May 8th, 2009");
		assertEquals("2009-05-08 00:00:00", DateUtil.date(parse).toString());

		parse = parser.parse("May 8th, 09");
		assertEquals("2009-05-08 00:00:00", DateUtil.date(parse).toString());

		parse = parser.parse("may. 8th, 09");
		assertEquals("2009-05-08 00:00:00", DateUtil.date(parse).toString());
	}

	private static void assertMatch(final String regex, final String dateStr){
		assertTrue(ReUtil.isMatch(Pattern.compile(regex, Pattern.CASE_INSENSITIVE), dateStr));
	}
}
