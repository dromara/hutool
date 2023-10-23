package cn.hutool.core.date;

import cn.hutool.core.lang.Console;
import org.junit.Assert;
import org.junit.Test;

public class Issue3348Test {

	@Test
	public void formatChineseDateTest() {
		final String formatChineseDate = DateUtil.formatChineseDate(
			DateUtil.parse("2023-10-23"), true, false);
		Console.log(formatChineseDate);
		Assert.assertEquals("二〇二三年十月二十三日", formatChineseDate);
	}
}
