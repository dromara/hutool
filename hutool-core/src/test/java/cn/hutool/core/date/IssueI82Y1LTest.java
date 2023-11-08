package cn.hutool.core.date;

import org.junit.Assert;
import org.junit.Test;

public class IssueI82Y1LTest {
	@Test
	public void parseTest() {
		final String dt1 = "2023-09-14T05:00:03.648519Z";
		Assert.assertEquals("2023-09-14 05:10:51", DateUtil.parse(dt1).toString());
	}
}
