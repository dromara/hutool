package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;
import cn.hutool.core.lang.Console;
import org.junit.Test;

public class IssueI5YB1ATest {
	@Test
	public void chineseDateTest() {
		final ChineseDate date = new ChineseDate(2023, 4, 8, true);
		Console.log(date.getGregorianDate());
	}
}
