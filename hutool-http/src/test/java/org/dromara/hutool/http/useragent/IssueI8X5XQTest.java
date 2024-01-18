package org.dromara.hutool.http.useragent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI8X5XQTest {

	@Test
	public void parseTest() {
		final String s = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 " +
			"Safari/537.36 Core/1.94.218.400 QQBrowser/12.1.5496.400";
		final UserAgent ua2 = UserAgentUtil.parse(s);

		Assertions.assertEquals("QQBrowser", ua2.getBrowser().toString());
		Assertions.assertEquals("12.1.5496.400", ua2.getVersion());
		Assertions.assertEquals("Webkit", ua2.getEngine().toString());
		Assertions.assertEquals("537.36", ua2.getEngineVersion());
		Assertions.assertEquals("Windows 10 or Windows Server 2016", ua2.getOs().toString());
		Assertions.assertEquals("10.0", ua2.getOsVersion());
		Assertions.assertEquals("Windows", ua2.getPlatform().toString());
		Assertions.assertFalse(ua2.isMobile());
	}
}
