package cn.hutool.core.date;

import cn.hutool.core.lang.Console;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class Issue3348Test {

	@Test
	public void formatChineseDateTest() {
		final String formatChineseDate = DateUtil.formatChineseDate(
			DateUtil.parse("2023-10-23"), true, false);
		Console.log(formatChineseDate);
		assertEquals("二〇二三年十月二十三日", formatChineseDate);
	}
}
