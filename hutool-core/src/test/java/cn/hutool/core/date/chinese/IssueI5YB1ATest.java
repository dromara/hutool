package cn.hutool.core.date.chinese;

import cn.hutool.core.date.ChineseDate;
import org.junit.Assert;
import org.junit.Test;

public class IssueI5YB1ATest {
	@Test
		public void chineseDateTest() {
		// 四月非闰月，因此isLeapMonth参数无效
		final ChineseDate date = new ChineseDate(2023, 4, 8, true);
		Assert.assertEquals("2023-05-26 00:00:00", date.getGregorianDate().toString());
	}
}
