package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

public class IssueI9C2D4Test {
	@Test
	public void parseHttpTest() {
		String dateStr = "Thu, 28 Mar 2024 14:33:49 GMT";
		final DateTime parse = DateUtil.parse(dateStr);
		Assert.assertEquals("2024-03-28 14:33:49", parse.toString());
	}

	@Test
	public void parseHttpTest2() {
		String dateStr = "星期四, 28 三月 2024 14:33:49 GMT";
		final DateTime parse = DateUtil.parse(dateStr);
		Assert.assertEquals("2024-03-28 14:33:49", parse.toString());
	}

	@Test
	public void parseTimeTest() {
		String dateStr = "15时45分59秒";
		final DateTime parse = DateUtil.parse(dateStr);
		Assert.assertEquals("15:45:59", parse.toString().split(" ")[1]);
	}

	@Test
	public void parseTimeTest2() {
		String dateStr = "15:45:59";
		final DateTime parse = DateUtil.parse(dateStr);
		Assert.assertEquals("15:45:59", parse.toString().split(" ")[1]);
	}
}
