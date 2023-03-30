package cn.hutool.core.date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Issue2981Test {
	/**
	 * https://github.com/dromara/hutool/issues/2981<br>
	 * 按照ISO8601规范，以Z结尾表示UTC时间，否则为当地时间
	 */
	@SuppressWarnings("DataFlowIssue")
	@Test
	public void parseUTCTest() {
		final String str1 = "2019-01-01T00:00:00.000Z";
		final String str2 = "2019-01-01T00:00:00.000";
		final String str3 = "2019-01-01 00:00:00.000";

		Assertions.assertEquals(1546300800000L, DateUtil.parse(str1).getTime());
		Assertions.assertEquals(1546272000000L, DateUtil.parse(str2).getTime());
		Assertions.assertEquals(1546272000000L, DateUtil.parse(str3).getTime());
	}
}
