package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.TimeZone;

public class IssueIBB6I5Test {
	@Test
	void parseISO8601Test() {
		DateTime date = DateUtil.parseISO8601("2024-12-13T08:02:27Z");
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"));
		date.setTimeZone(timeZone);
		Assertions.assertEquals("2024-12-13 16:02:27", date.toString());
	}

	@Test
	void parseISO8601Test2() {
		DateTime date = DateUtil.parseISO8601("2024-12-13T08:02:27");
		TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Asia/Shanghai"));
		date.setTimeZone(timeZone);
		Assertions.assertEquals("2024-12-13 08:02:27", date.toString());
	}
}
