package org.dromara.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Objects;

public class IssueI9C2D4Test {
	@Test
	public void parseHttpTest() {
		final String dateStr = "Thu, 28 Mar 2024 14:33:49 GMT";
		final Date parse = DateUtil.parse(dateStr);
		Assertions.assertEquals("2024-03-28 22:33:49", Objects.requireNonNull(parse).toString());
	}

	@Test
	public void parseHttpTest2() {
		final String dateStr = "星期四, 28 三月 2024 14:33:49 GMT";
		final Date parse = DateUtil.parse(dateStr);
		Assertions.assertEquals("2024-03-28 22:33:49", Objects.requireNonNull(parse).toString());
	}

	@Test
	public void parseTimeTest() {
		final String dateStr = "15时45分59秒";
		final Date parse = DateUtil.parse(dateStr);
		Assertions.assertEquals("15:45:59", Objects.requireNonNull(parse).toString().split(" ")[1]);
	}

	@Test
	public void parseTimeTest2() {
		final String dateStr = "15:45:59";
		final Date parse = DateUtil.parse(dateStr);
		Assertions.assertEquals("15:45:59", Objects.requireNonNull(parse).toString().split(" ")[1]);
	}
}
